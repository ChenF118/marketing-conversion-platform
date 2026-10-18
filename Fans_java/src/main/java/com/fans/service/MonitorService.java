package com.fans.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fans.common.Api;
import com.fans.common.CurrentUser;
import com.fans.common.Maps;
import com.fans.common.Req;
import com.fans.entity.Group;
import com.fans.entity.Landing;
import com.fans.entity.Monitor;
import com.fans.entity.Weixin;
import com.fans.mapper.GroupMapper;
import com.fans.mapper.LandingMapper;
import com.fans.mapper.MonitorMapper;
import com.fans.mapper.WeixinMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/** 行为追踪：写入监测记录、自增计数、查询与首页趋势 */
@Service
public class MonitorService {

    private static final DateTimeFormatter DAY = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final MonitorMapper monitorMapper;
    private final LandingMapper landingMapper;
    private final WeixinMapper weixinMapper;
    private final GroupMapper groupMapper;
    private final ObjectMapper om;
    private final Maps maps;

    public MonitorService(MonitorMapper monitorMapper, LandingMapper landingMapper,
                          WeixinMapper weixinMapper, GroupMapper groupMapper,
                          ObjectMapper om, Maps maps) {
        this.monitorMapper = monitorMapper;
        this.landingMapper = landingMapper;
        this.weixinMapper = weixinMapper;
        this.groupMapper = groupMapper;
        this.om = om;
        this.maps = maps;
    }

    /**
     * 写入一条行为记录并自增落地页/微信号计数。
     * - eventId 存在时做幂等：重复事件直接忽略，不再累加计数
     * - cvsdate/cvstime 规范化为 DATE/TIME 可接受的值
     */
    @Transactional
    public void append(Req req) {
        Integer landingId = Req.toInteger(req.get("url"));
        if (landingId == null) {
            throw new IllegalArgumentException("url（落地页ID）不能为空");
        }
        Integer type = req.integer("type");
        if (type == null || type < 1 || type > 17) {
            throw new IllegalArgumentException("type 必须在 1-17 之间");
        }

        String eventId = req.str("eventId");
        if (eventId != null && eventId.length() > 64) {
            eventId = eventId.substring(0, 64);
        }
        if (eventId != null && !eventId.isEmpty()) {
            Long exists = monitorMapper.selectCount(new QueryWrapper<Monitor>().eq("eventId", eventId));
            if (exists != null && exists > 0) return;
        }

        Monitor monitor = new Monitor();
        monitor.eventId = eventId;
        monitor.uid = landingId;
        monitor.cvsdate = normalizeDate(req.str("cvsdate"));
        monitor.cvstime = normalizeTime(req.str("cvstime"));
        monitor.stay = req.integer("stay");
        monitor.type = type;
        monitor.target = req.str("targetelement");
        monitor.city = toJsonText(req.get("city"));
        monitor.equipment = req.str("equipment");
        monitor.rate = req.str("rate");
        monitor.wx = req.str("wx");
        monitor.search = req.str("search");
        monitor.total = req.integer("total");
        monitor.report = req.integer("report");

        try {
            monitorMapper.insert(monitor);
        } catch (DuplicateKeyException e) {
            return; // 并发下同一 eventId 只记一次
        }

        landingMapper.update(null, new UpdateWrapper<Landing>()
                .eq("id", landingId)
                .setSql("total = total + 1")
                .setSql("cvscount = cvscount + 1")
                .set("neardate", LocalDateTime.now())
                .set("updateTime", LocalDateTime.now()));

        if (monitor.wx != null && !monitor.wx.isEmpty()) {
            weixinMapper.update(null, new UpdateWrapper<Weixin>()
                    .eq("wxh", monitor.wx)
                    .setSql("visits = visits + 1")
                    .setSql("copyCount = copyCount + 1")
                    .set("updateTime", LocalDateTime.now()));
        }
    }

    @Transactional
    public void incTotal(Req req) {
        Integer landingId = req.integer("url");
        if (landingId == null) {
            throw new IllegalArgumentException("url（落地页ID）不能为空");
        }
        String wx = req.str("wx");
        landingMapper.update(null, new UpdateWrapper<Landing>()
                .eq("id", landingId)
                .setSql("total = total + 1")
                .set("updateTime", LocalDateTime.now()));
        if (wx != null && !wx.isEmpty()) {
            weixinMapper.update(null, new UpdateWrapper<Weixin>()
                    .eq("wxh", wx)
                    .setSql("visits = visits + 1")
                    .set("updateTime", LocalDateTime.now()));
        }
    }

    public Map<String, Object> getMonitor(Req req) {
        Map<String, Object> filter = req.map("condition");
        long current = req.longVal("currentpage", 1);
        long size = req.longVal("singlepage", 10);

        Map<String, Object> q = new HashMap<>();
        if (Req.truthy(filter.get("url"))) q.put("uid", Req.toInteger(filter.get("url")));
        if (Req.truthy(filter.get("date1"))) q.put("date1", Req.text(filter.get("date1")));
        if (Req.truthy(filter.get("date2"))) q.put("date2", Req.text(filter.get("date2")));
        if (Req.truthy(filter.get("state"))) q.put("report", Req.toInteger(filter.get("state")));
        if (Req.truthy(filter.get("words"))) q.put("urlLike", Req.text(filter.get("words")));
        if (Req.truthy(filter.get("group"))) q.put("gid", Req.toInteger(filter.get("group")));
        if (!CurrentUser.isAdmin()) q.put("ownerId", CurrentUser.id());

        List<Monitor> list = monitorMapper.selectPageWithLanding(q, Math.max(0, (current - 1) * size), size);
        long total = monitorMapper.countWithLanding(q);

        List<Map<String, Object>> rows = maps.ofList(list);
        attachLanding(rows);
        return Api.page(rows, total);
    }

    /** 最近 7 天（不含今天）的 PV/UV/转化数 */
    public Map<String, Object> home() {
        Integer ownerId = CurrentUser.isAdmin() ? null : CurrentUser.id();
        List<Object> data = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for (int i = 0; i <= 6; i++) {
            String day = today.minusDays(7 - i).format(DAY);
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("pv", monitorMapper.countPv(day, ownerId));
            item.put("uv", monitorMapper.countUv(day, ownerId));
            item.put("count", monitorMapper.countConverted(day, ownerId));
            data.add(List.of(day, item));
        }
        return Api.ok("获取成功！", data);
    }

    // ------------------------------------------------------------ 内部

    private String normalizeDate(String value) {
        if (value == null || value.isBlank()) return LocalDate.now().format(DAY);
        try {
            return LocalDate.parse(value.trim(), DAY).format(DAY);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("cvsdate 格式应为 yyyy-MM-dd");
        }
    }

    private String normalizeTime(String value) {
        if (value == null || value.isBlank()) return null;
        String text = value.trim();
        if (text.length() == 5) text = text + ":00";
        try {
            return LocalTime.parse(text, TIME).format(TIME);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("cvstime 格式应为 HH:mm:ss");
        }
    }

    private String toJsonText(Object city) {
        if (city == null) return null;
        if (city instanceof String s) return s;
        try {
            return om.writeValueAsString(city);
        } catch (Exception e) {
            return String.valueOf(city);
        }
    }

    private void attachLanding(List<Map<String, Object>> rows) {
        List<Integer> ids = rows.stream().map(r -> Req.toInteger(r.get("uid")))
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) return;

        Map<Integer, Landing> landings = landingMapper.selectBatchIds(ids).stream()
                .collect(Collectors.toMap(l -> l.id, l -> l));
        List<Integer> groupIds = landings.values().stream().map(l -> l.uid)
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Integer, Group> groups = groupIds.isEmpty() ? Collections.emptyMap()
                : groupMapper.selectBatchIds(groupIds).stream().collect(Collectors.toMap(g -> g.id, g -> g));

        for (Map<String, Object> row : rows) {
            Landing landing = landings.get(Req.toInteger(row.get("uid")));
            if (landing == null) {
                row.put("Landing", null);
                continue;
            }
            Map<String, Object> lm = new LinkedHashMap<>();
            lm.put("id", landing.id);
            lm.put("uid", landing.uid);
            lm.put("url", landing.url);
            lm.put("remarks", landing.remarks);
            Group g = groups.get(landing.uid);
            lm.put("Group", g == null ? null : Maps.pick("id", g.id, "group", g.groupName));
            row.put("Landing", lm);
        }
    }
}
