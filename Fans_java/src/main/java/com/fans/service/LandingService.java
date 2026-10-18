package com.fans.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fans.common.Api;
import com.fans.common.CurrentUser;
import com.fans.common.ForbiddenException;
import com.fans.common.Maps;
import com.fans.common.Req;
import com.fans.entity.Cvslink;
import com.fans.entity.Group;
import com.fans.entity.Landing;
import com.fans.entity.Wxgroup;
import com.fans.mapper.CvslinkMapper;
import com.fans.mapper.GroupMapper;
import com.fans.mapper.LandingMapper;
import com.fans.mapper.WxgroupMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/** 落地页 */
@Service
public class LandingService {

    private static final Set<String> RANK_ORDER_COLUMNS =
            Set.of("id", "url", "remarks", "total", "cvscount", "fans", "uid", "neardate");

    /** 与前端 utils/validate.js 的 validateUrl 保持一致 */
    private static final Pattern URL_PATTERN =
            Pattern.compile("^(?:http(s)?://)?[\\w.-]+(?:\\.[\\w-]+)+[\\w\\-_~:/?#\\[\\]@!$&',;=.]+$");

    private static final int MAX_BATCH = 500;

    private final LandingMapper landingMapper;
    private final GroupMapper groupMapper;
    private final WxgroupMapper wxgroupMapper;
    private final CvslinkMapper cvslinkMapper;
    private final Maps maps;

    public LandingService(LandingMapper landingMapper, GroupMapper groupMapper,
                          WxgroupMapper wxgroupMapper, CvslinkMapper cvslinkMapper, Maps maps) {
        this.landingMapper = landingMapper;
        this.groupMapper = groupMapper;
        this.wxgroupMapper = wxgroupMapper;
        this.cvslinkMapper = cvslinkMapper;
        this.maps = maps;
    }

    // ------------------------------------------------------------ 写

    @Transactional
    public Map<String, Object> append(Map<String, Object> data) {
        String url = Req.text(data.get("landingurl"));
        QueryWrapper<Landing> existsQuery = new QueryWrapper<Landing>().like("url", url == null ? "" : url);
        applyOwner(existsQuery);
        if (landingMapper.selectCount(existsQuery) > 0) {
            return Api.res(201, "该落地页链接已存在！", List.of());
        }
        Landing landing = new Landing();
        landing.ownerId = CurrentUser.id();
        landing.url = url;
        landing.uid = Req.toInteger(data.get("group"));
        landing.remarks = Req.text(data.get("remarks"));
        landing.delivery = Req.toInteger(data.get("delivery"));
        landing.track = Req.toInteger(data.get("track"));
        landing.enable = Req.toInteger(data.get("open"));
        landingMapper.insert(landing);
        return Api.ok("添加成功！");
    }

    // ------------------------------------------------------------ 批量导入

    public Map<String, Object> importPreview(Map<String, Object> data) {
        return doImport(data, true);
    }

    @Transactional
    public Map<String, Object> importCommit(Map<String, Object> data) {
        return doImport(data, false);
    }

    /**
     * 一行一个 URL，支持预览（dryRun=true）与正式导入。
     * 判重规则与单条新增一致：已有 URL LIKE '%新URL%' 即视为重复；
     * 同一次粘贴内的重复也会被识别。
     */
    private Map<String, Object> doImport(Map<String, Object> data, boolean dryRun) {
        String raw = Req.text(data.get("landingurls"));
        if (raw == null || raw.isBlank()) raw = Req.text(data.get("landingurl"));
        if (raw == null || raw.isBlank()) {
            return Api.res(400, "请填写落地页URL", List.of());
        }

        List<Map<String, Object>> duplicates = new ArrayList<>();
        List<Map<String, Object>> invalid = new ArrayList<>();
        List<String> valid = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        List<String> existing = landingMapper.selectList(
                        new QueryWrapper<Landing>().select("url")).stream()
                .map(l -> l.url == null ? "" : l.url.toLowerCase(Locale.ROOT))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

        String[] lines = raw.split("\\r?\\n", -1);
        int total = 0;
        for (int i = 0; i < lines.length; i++) {
            String url = lines[i].trim();
            if (url.isEmpty()) continue;
            total++;
            if (total > MAX_BATCH) {
                return Api.res(400, "一次最多导入 " + MAX_BATCH + " 条", List.of());
            }
            if (!URL_PATTERN.matcher(url).matches()) {
                invalid.add(Maps.pick("line", i + 1, "url", url, "reason", "格式不正确"));
                continue;
            }
            String key = url.toLowerCase(Locale.ROOT);
            if (seen.contains(key)) {
                duplicates.add(Maps.pick("line", i + 1, "url", url, "reason", "本次粘贴内重复"));
                continue;
            }
            boolean exists = existing.stream().anyMatch(e -> e.contains(key));
            if (exists) {
                duplicates.add(Maps.pick("line", i + 1, "url", url, "reason", "已存在"));
                continue;
            }
            seen.add(key);
            valid.add(url);
        }

        int inserted = 0;
        if (!dryRun) {
            Integer uid = Req.toInteger(data.get("group"));
            Integer delivery = Req.toInteger(data.get("delivery"));
            Integer track = Req.toInteger(data.get("track"));
            Integer open = Req.toInteger(data.get("open"));
            String remarks = Req.text(data.get("remarks"));
            for (String url : valid) {
                Landing landing = new Landing();
                landing.ownerId = CurrentUser.id();
                landing.url = url;
                landing.uid = uid;
                landing.remarks = remarks;
                landing.delivery = delivery;
                landing.track = track;
                landing.enable = open;
                landingMapper.insert(landing);
                inserted++;
            }
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("total", total);
        result.put("validCount", valid.size());
        result.put("validUrls", valid);
        result.put("duplicates", duplicates);
        result.put("invalid", invalid);
        if (!dryRun) result.put("inserted", inserted);
        return Api.res(200, dryRun ? "预览成功" : "导入成功", result);
    }

    @Transactional
    public Map<String, Object> update(Map<String, Object> data) {
        Integer id = Req.toInteger(data.get("id"));
        String url = Req.text(data.get("url"));
        QueryWrapper<Landing> existsQuery = new QueryWrapper<Landing>()
                .like("url", url == null ? "" : url)
                .ne("id", id);
        applyOwner(existsQuery);
        if (landingMapper.selectCount(existsQuery) > 0) {
            return Api.res(201, "该落地页链接已存在！", List.of());
        }
        Landing landing = requireOwned(id);
        landing.url = url;
        landing.uid = Req.toInteger(data.get("group"));
        landing.remarks = Req.text(data.get("remarks"));
        landing.delivery = Req.toInteger(data.get("delivery"));
        landing.track = Req.toInteger(data.get("track"));
        landing.enable = Req.toInteger(data.get("enable"));
        landingMapper.updateById(landing);
        return Api.ok("修改成功！");
    }

    public Map<String, Object> delete(Integer id) {
        if (id == null) return Api.ok("删除成功", 0);
        requireOwned(id);
        return Api.ok("删除成功", landingMapper.deleteById(id));
    }

    public Map<String, Object> deleteLot(List<Integer> ids) {
        if (ids.isEmpty()) return Api.ok("删除成功", 0);
        QueryWrapper<Landing> owned = new QueryWrapper<Landing>().in("id", ids);
        applyOwner(owned);
        List<Integer> deleteIds = landingMapper.selectList(owned).stream().map(l -> l.id).collect(Collectors.toList());
        int n = deleteIds.isEmpty() ? 0 : landingMapper.deleteByIds(deleteIds);
        return Api.ok("删除成功", n);
    }

    public Map<String, Object> toggle(Integer id, Integer enable) {
        requireOwned(id);
        Landing landing = new Landing();
        landing.id = id;
        landing.enable = enable;
        landingMapper.updateById(landing);
        return Api.ok("修改成功！");
    }

    @Transactional
    public Map<String, Object> unbind(Integer id) {
        requireOwned(id);
        applyUnbind(new UpdateWrapper<Landing>().eq("id", id));
        return Api.ok("修改成功！");
    }

    @Transactional
    public Map<String, Object> unbindLot(List<Integer> ids) {
        if (ids.isEmpty()) return Api.ok("修改成功！", 0);
        UpdateWrapper<Landing> wrapper = new UpdateWrapper<Landing>().in("id", ids);
        if (!CurrentUser.isAdmin()) wrapper.eq("ownerId", CurrentUser.id());
        applyUnbind(wrapper);
        return Api.ok("修改成功！", ids.size());
    }

    private void applyUnbind(UpdateWrapper<Landing> wrapper) {
        wrapper.set("gid", null)
                .set("same", "")
                .set("share", null)
                .set("mclick", "")
                .set("mtouch", "")
                .set("pclick", "")
                .set("pselect", "")
                .set("sourceUrl", "")
                .set("visible", null)
                .set("updateTime", LocalDateTime.now());
        landingMapper.update(null, wrapper);
    }

    @Transactional
    public Map<String, Object> changeWeixin(Map<String, Object> data) {
        Integer id = Req.toInteger(data.get("id"));
        Landing landing = requireOwned(id);
        landing.erweima = Req.text(data.get("erweima"));
        landing.sourceUrl = Req.text(data.get("from"));
        landing.gid = Req.toInteger(data.get("group"));
        landing.same = Req.text(data.get("same"));
        landing.weixin = Req.text(data.get("weixin"));
        landing.mclick = Req.text(data.get("mclick"));
        landing.mtouch = Req.text(data.get("mtouch"));
        landing.pclick = Req.text(data.get("pclick"));
        landing.pselect = Req.text(data.get("pselect"));
        landing.visible = (Req.truthy(data.get("show1")) || Req.truthy(data.get("show2"))) ? 1 : 0;
        landing.share = Req.toInteger(data.get("share"));
        landingMapper.updateById(landing);
        return Api.ok("修改成功！");
    }

    @Transactional
    public Map<String, Object> changeLot(List<Integer> ids, Map<String, Object> form) {
        if (ids.isEmpty()) return Api.ok("修改成功！");
        boolean show = Req.truthy(form.get("show1")) || Req.truthy(form.get("show2"));
        UpdateWrapper<Landing> wrapper = new UpdateWrapper<Landing>().in("id", ids);
        if (!CurrentUser.isAdmin()) wrapper.eq("ownerId", CurrentUser.id());
        wrapper.set("erweima", Req.text(form.get("erweima")))
                .set("sourceUrl", Req.text(form.get("from")))
                .set("gid", Req.toInteger(form.get("group")))
                .set("same", Req.text(form.get("same")))
                .set("weixin", Req.text(form.get("weixin")))
                .set("mclick", Req.text(form.get("mclick")))
                .set("mtouch", Req.text(form.get("mtouch")))
                .set("pclick", Req.text(form.get("pclick")))
                .set("pselect", Req.text(form.get("pselect")))
                .set("share", Req.toInteger(form.get("share")))
                .set("visible", show ? 1 : 0)
                .set("updateTime", LocalDateTime.now());
        landingMapper.update(null, wrapper);
        return Api.ok("修改成功！");
    }

    // ------------------------------------------------------------ 读

    public Map<String, Object> getLanding(Req req) {
        Map<String, Object> filter = req.map("condition");
        long current = req.longVal("currentpage", 1);
        long size = req.longVal("singlepage", 10);

        QueryWrapper<Landing> wrapper = landingQuery(filter);
        Page<Landing> page = landingMapper.selectPage(new Page<>(current, size), wrapper.orderByAsc("id"));
        long total = landingMapper.selectCount(landingQuery(filter));

        List<Map<String, Object>> rows = maps.ofList(page.getRecords());
        attachGroups(rows);
        rows.forEach(m -> Maps.without(m, "total", "neardate", "cvscount"));
        return Api.page(rows, total);
    }

    public Map<String, Object> getLandingAll() {
        QueryWrapper<Landing> wrapper = new QueryWrapper<Landing>().select("id", "url").orderByAsc("id");
        applyOwner(wrapper);
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Landing l : landingMapper.selectList(wrapper)) {
            rows.add(Maps.pick("id", l.id, "url", l.url));
        }
        return Api.ok("成功", rows);
    }

    /** LandingCt::landingandgroup —— 直接返回数组（无 code/message 包装） */
    public List<Map<String, Object>> landingAndGroup() {
        QueryWrapper<Landing> listQuery = new QueryWrapper<Landing>().orderByAsc("id");
        applyOwner(listQuery);
        List<Landing> landings = landingMapper.selectList(listQuery);
        List<Map<String, Object>> rows = maps.ofList(landings);
        if (landings.isEmpty()) return rows;

        List<Integer> ids = landings.stream().map(l -> l.id).collect(Collectors.toList());
        List<Cvslink> links = cvslinkMapper.selectList(new QueryWrapper<Cvslink>().in("uid", ids));
        Map<Integer, Cvslink> byUid = links.stream()
                .collect(Collectors.toMap(c -> c.uid, Function.identity(), (a, b) -> a));

        for (int i = 0; i < landings.size(); i++) {
            Cvslink link = byUid.get(landings.get(i).id);
            if (link != null) {
                rows.get(i).put("cvslink", Maps.pick("uid", link.uid));
            } else {
                rows.get(i).put("cvslink", null);
            }
        }
        return rows;
    }

    public Map<String, Object> landingWithGroup(Req req) {
        Map<String, Object> filter = req.map("condition");
        long current = req.longVal("currentpage", 1);
        long size = req.longVal("singlepage", 10);

        QueryWrapper<Landing> wrapper = landingQuery(filter);
        Page<Landing> page = landingMapper.selectPage(new Page<>(current, size), wrapper.orderByAsc("id"));
        long total = landingMapper.selectCount(landingQuery(filter));

        List<Map<String, Object>> rows = maps.ofList(page.getRecords());
        attachWxgroups(rows);
        rows.forEach(m -> Maps.without(m, "total", "neardate", "cvscount", "delivery", "track", "enable"));
        return Api.page(rows, total);
    }

    public Map<String, Object> landingRank(Req req) {
        Map<String, Object> filter = req.map("condition");
        long current = req.longVal("currentpage", 1);
        long size = req.longVal("singlepage", 10);

        QueryWrapper<Landing> wrapper = new QueryWrapper<>();
        Integer uid = Req.toInteger(filter.get("group"));
        if (uid != null) wrapper.eq("uid", uid);
        if (Req.truthy(filter.get("url"))) wrapper.like("url", Req.text(filter.get("url")));
        if (Req.truthy(filter.get("date1"))) wrapper.ge("neardate", Req.text(filter.get("date1")));
        if (Req.truthy(filter.get("date2"))) wrapper.le("neardate", Req.text(filter.get("date2")));

        applyOwner(wrapper);
        long total = landingMapper.selectCount(wrapper);

        String order = Req.text(filter.get("order"));
        if (order == null || !RANK_ORDER_COLUMNS.contains(order)) order = "id";

        QueryWrapper<Landing> pageQuery = new QueryWrapper<Landing>()
                .select("id", "url", "remarks", "total", "cvscount", "fans", "uid")
                .orderByDesc(order);
        if (uid != null) pageQuery.eq("uid", uid);
        if (Req.truthy(filter.get("url"))) pageQuery.like("url", Req.text(filter.get("url")));
        if (Req.truthy(filter.get("date1"))) pageQuery.ge("neardate", Req.text(filter.get("date1")));
        if (Req.truthy(filter.get("date2"))) pageQuery.le("neardate", Req.text(filter.get("date2")));
        applyOwner(pageQuery);
        pageQuery.last("LIMIT " + Math.max(0, (current - 1) * size) + ", " + size);

        List<Map<String, Object>> rows = new ArrayList<>();
        for (Landing l : landingMapper.selectList(pageQuery)) {
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", l.id);
            m.put("url", l.url);
            m.put("remarks", l.remarks);
            m.put("total", l.total);
            m.put("cvscount", l.cvscount);
            m.put("fans", l.fans);
            m.put("uid", l.uid);
            rows.add(m);
        }
        attachGroups(rows);
        return Api.page(rows, total);
    }

    /**
     * landing/gtr：按转化率页所需的数据结构返回 total/cvscount + rate。
     */
    public Map<String, Object> landingRate(Req req) {
        Map<String, Object> filter = req.map("condition");
        long current = req.longVal("currentpage", 1);
        long size = req.longVal("singlepage", 10);

        QueryWrapper<Landing> wrapper = new QueryWrapper<>();
        Integer uid = Req.toInteger(filter.get("group"));
        if (uid != null) wrapper.eq("uid", uid);
        if (Req.truthy(filter.get("url"))) wrapper.like("url", Req.text(filter.get("url")));
        applyOwner(wrapper);

        long total = landingMapper.selectCount(wrapper);
        wrapper.select("id", "url", "total", "cvscount", "fans", "uid").orderByAsc("id")
                .last("LIMIT " + Math.max(0, (current - 1) * size) + ", " + size);

        List<Map<String, Object>> rows = new ArrayList<>();
        for (Landing l : landingMapper.selectList(wrapper)) {
            long cv = l.cvscount == null ? 0 : l.cvscount;
            long tp = l.total == null ? 0 : l.total;
            String rate = tp == 0 ? "0.00%" : String.format("%.2f%%", cv * 100.0 / tp);
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", l.id);
            m.put("url", l.url);
            m.put("total", l.total);
            m.put("cvscount", l.cvscount);
            m.put("fans", l.fans);
            m.put("uid", l.uid);
            m.put("rate", rate);
            rows.add(m);
        }
        attachGroups(rows);
        return Api.page(rows, total);
    }

    // ------------------------------------------------------------ 内部

    private QueryWrapper<Landing> landingQuery(Map<String, Object> filter) {
        QueryWrapper<Landing> wrapper = new QueryWrapper<>();
        Integer uid = Req.toInteger(filter.get("group"));
        if (uid != null) wrapper.eq("uid", uid);
        Object enable = filter.get("enable");
        if (enable != null && !"".equals(String.valueOf(enable))) {
            wrapper.eq("enable", Req.toInteger(enable));
        }
        if (Req.truthy(filter.get("words"))) wrapper.like("url", Req.text(filter.get("words")));
        applyOwner(wrapper);
        return wrapper;
    }

    private void applyOwner(QueryWrapper<Landing> wrapper) {
        if (!CurrentUser.isAdmin()) {
            wrapper.eq("ownerId", CurrentUser.id());
        }
    }

    private Landing requireOwned(Integer id) {
        Landing landing = landingMapper.selectById(id);
        if (landing == null) throw new IllegalArgumentException("记录不存在");
        if (!CurrentUser.canAccess(landing.ownerId)) throw new ForbiddenException("无权操作该数据");
        return landing;
    }

    private void attachGroups(List<Map<String, Object>> rows) {
        List<Integer> ids = rows.stream()
                .map(r -> Req.toInteger(r.get("uid")))
                .filter(java.util.Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, Group> groups = ids.isEmpty() ? Collections.emptyMap()
                : groupMapper.selectBatchIds(ids).stream().collect(Collectors.toMap(g -> g.id, Function.identity()));
        for (Map<String, Object> row : rows) {
            Group g = groups.get(Req.toInteger(row.get("uid")));
            row.put("Group", g == null ? null : Maps.pick("id", g.id, "group", g.groupName));
        }
    }

    private void attachWxgroups(List<Map<String, Object>> rows) {
        List<Integer> ids = rows.stream()
                .map(r -> Req.toInteger(r.get("gid")))
                .filter(java.util.Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        Map<Integer, Wxgroup> groups = ids.isEmpty() ? Collections.emptyMap()
                : wxgroupMapper.selectBatchIds(ids).stream().collect(Collectors.toMap(g -> g.id, Function.identity()));
        for (Map<String, Object> row : rows) {
            Wxgroup g = groups.get(Req.toInteger(row.get("gid")));
            row.put("wxgroup", g == null ? null : Maps.pick("id", g.id, "name", g.name));
        }
    }
}
