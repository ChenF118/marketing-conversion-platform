package com.fans.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fans.common.Api;
import com.fans.common.CurrentUser;
import com.fans.common.ForbiddenException;
import com.fans.common.Maps;
import com.fans.common.Req;
import com.fans.entity.Cvslink;
import com.fans.entity.Group;
import com.fans.entity.Landing;
import com.fans.mapper.CvslinkMapper;
import com.fans.mapper.GroupMapper;
import com.fans.mapper.LandingMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/** 统计链接 */
@Service
public class CvslinkService {

    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final CvslinkMapper cvslinkMapper;
    private final LandingMapper landingMapper;
    private final GroupMapper groupMapper;
    private final com.fans.mapper.WxgroupMapper wxgroupMapper;
    private final com.fans.mapper.WeixinMapper weixinMapper;
    private final Maps maps;

    public CvslinkService(CvslinkMapper cvslinkMapper, LandingMapper landingMapper,
                          GroupMapper groupMapper,
                          com.fans.mapper.WxgroupMapper wxgroupMapper,
                          com.fans.mapper.WeixinMapper weixinMapper,
                          Maps maps) {
        this.cvslinkMapper = cvslinkMapper;
        this.landingMapper = landingMapper;
        this.groupMapper = groupMapper;
        this.wxgroupMapper = wxgroupMapper;
        this.weixinMapper = weixinMapper;
        this.maps = maps;
    }

    @Transactional
    public Map<String, Object> append(Map<String, Object> data) {
        Integer uid = Req.toInteger(data.get("uid"));
        String type = Req.text(data.get("type"));
        Landing landing = landingMapper.selectById(uid);
        if (landing == null || !CurrentUser.canAccess(landing.ownerId)) {
            throw new ForbiddenException("无权在该落地页下创建统计链接");
        }
        Cvslink existed = cvslinkMapper.selectOne(
                new QueryWrapper<Cvslink>().eq("uid", uid).last("LIMIT 1"));
        if (existed != null && Objects.equals(existed.type, type)) {
            return Api.res(201, "该统计链接已存在！请编辑", List.of());
        }
        Cvslink link = new Cvslink();
        link.ownerId = CurrentUser.id();
        link.uid = uid;
        link.type = type;
        link.cvstype = Req.toInteger(data.get("cvstype"));
        link.cvscount = Req.toInteger(data.get("cvscount"));
        link.cvsmode = Req.toInteger(data.get("cvsmode"));
        link.code = Req.text(data.get("code"));
        cvslinkMapper.insert(link);
        return Api.ok("添加成功！");
    }

    @Transactional
    public Map<String, Object> delete(Req req) {
        List<Integer> ids = new ArrayList<>();
        if (req.has("id") && req.integer("id") != null) {
            ids.add(req.integer("id"));
        } else if (req.has("arr")) {
            ids.addAll(req.ints("arr"));
        } else {
            for (Object v : req.raw().values()) {
                if (v instanceof List<?> list) {
                    for (Object o : list) {
                        Integer i = Req.toInteger(o);
                        if (i != null) ids.add(i);
                    }
                } else {
                    Integer i = Req.toInteger(v);
                    if (i != null) ids.add(i);
                }
            }
        }
        int n = 0;
        for (Integer id : ids) {
            Cvslink link = cvslinkMapper.selectById(id);
            if (link != null && CurrentUser.canAccess(link.ownerId)) {
                n += cvslinkMapper.deleteById(id);
            }
        }
        return Api.ok("删除成功", n);
    }

    @Transactional
    public Map<String, Object> update(Map<String, Object> data) {
        Integer id = Req.toInteger(data.get("id"));
        Cvslink link = requireOwned(id);
        link.cvstype = Req.toInteger(data.get("cvstype"));
        link.cvscount = Req.toInteger(data.get("cvscount"));
        link.cvsmode = Req.toInteger(data.get("cvsmode"));
        link.type = Req.text(data.get("type"));
        link.code = Req.text(data.get("code"));
        cvslinkMapper.updateById(link);
        return Api.ok("修改成功！");
    }

    @Transactional
    public Map<String, Object> minSecond(Map<String, Object> data) {
        Integer id = Req.toInteger(data.get("id"));
        Cvslink link = requireOwned(id);
        link.second = Req.toInteger(data.get("second"));
        cvslinkMapper.updateById(link);
        return Api.ok("修改成功！");
    }

    /** CvslinkCt::getbelongs —— 关系键为小写 landing */
    public Map<String, Object> getBelongs(Req req) {
        Map<String, Object> filter = req.map("condition");
        long current = req.longVal("currentpage", 1);
        long size = req.longVal("singlepage", 10);

        Map<String, Object> q = new HashMap<>();
        if (Req.truthy(filter.get("group"))) q.put("uid", Req.toInteger(filter.get("group")));
        if (Req.truthy(filter.get("words"))) q.put("urlLike", Req.text(filter.get("words")));
        if (!CurrentUser.isAdmin()) q.put("ownerId", CurrentUser.id());

        List<Cvslink> links = cvslinkMapper.selectBelongsPage(q, Math.max(0, (current - 1) * size), size);
        long total = cvslinkMapper.countBelongs(q);

        Context ctx = context(links.stream().map(l -> l.uid).collect(Collectors.toList()));
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Cvslink link : links) {
            Map<String, Object> row = maps.of(link);
            row.put("landing", landingMap(ctx, link.uid, false));
            rows.add(row);
        }
        return Api.page(rows, total);
    }

    /** CvslinkCt::getcvsrate —— 只输出 cvstype + 大写 Landing */
    public Map<String, Object> getCvsRate(Req req) {
        Map<String, Object> filter = req.map("condition");
        long current = req.longVal("currentpage", 1);
        long size = req.longVal("singlepage", 10);

        Map<String, Object> q = new HashMap<>();
        if (Req.truthy(filter.get("group"))) q.put("uid", Req.toInteger(filter.get("group")));
        if (Req.truthy(filter.get("url"))) q.put("landingId", Req.toInteger(filter.get("url")));
        if (Req.truthy(filter.get("words"))) q.put("urlLike", Req.text(filter.get("words")));
        if (Req.truthy(filter.get("date1"))) q.put("date1", Req.text(filter.get("date1")));
        if (Req.truthy(filter.get("date2"))) q.put("date2", Req.text(filter.get("date2")));
        if (Req.truthy(filter.get("cvstype"))) q.put("cvstype", Req.toInteger(filter.get("cvstype")));
        if (!CurrentUser.isAdmin()) q.put("ownerId", CurrentUser.id());

        List<Cvslink> links = cvslinkMapper.selectRatePage(q, Math.max(0, (current - 1) * size), size);
        long total = cvslinkMapper.countRate(q);

        Context ctx = context(links.stream().map(l -> l.uid).collect(Collectors.toList()));
        List<Map<String, Object>> rows = new ArrayList<>();
        for (Cvslink link : links) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("cvstype", maps.of(link).get("cvstype"));
            row.put("Landing", landingMap(ctx, link.uid, true));
            rows.add(row);
        }
        return Api.page(rows, total);
    }

    /**
     * 落地页侧配置查询（CvslinkCt::forlandingget）。
     * 按 URL 找到启用中的落地页，返回其绑定的微信号分组与在线微信号。
     * 响应为裸 JSON（无 code/message 包装）。
     */
    public Object forLandingGet(Req req) {
        String words = req.str("words");
        if (words == null || words.isEmpty()) words = req.str("url");

        Cvslink link = cvslinkMapper.selectFirstForLanding(words);
        if (link == null) return null;

        Landing landing = landingMapper.selectById(link.uid);
        if (landing == null) return null;

        Map<String, Object> linkMap = maps.of(link);

        Map<String, Object> landingMap = new LinkedHashMap<>();
        landingMap.put("id", landing.id);
        landingMap.put("gid", landing.gid);

        Map<String, Object> wxgroupMap = null;
        if (landing.gid != null) {
            com.fans.entity.Wxgroup group = wxgroupMapper.selectById(landing.gid);
            if (group != null) {
                wxgroupMap = new LinkedHashMap<>();
                wxgroupMap.put("id", group.id);

                List<com.fans.entity.Weixin> list = weixinMapper.selectList(
                        new QueryWrapper<com.fans.entity.Weixin>()
                                .eq("uid", group.id)
                                .eq("online", 1)
                                .orderByDesc("level")
                                .orderByAsc("id"));
                List<Map<String, Object>> wxList = new ArrayList<>();
                for (com.fans.entity.Weixin w : list) {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", w.id);
                    m.put("uid", w.uid);
                    m.put("wxh", w.wxh);
                    m.put("erweima", w.erweima);
                    m.put("wxname", w.wxname);
                    m.put("online", w.online != null && w.online == 1);
                    m.put("level", w.level);
                    wxList.add(m);
                }
                wxgroupMap.put("Weixin", wxList);
            }
        }
        landingMap.put("Wxgroup", wxgroupMap);
        linkMap.put("landing", landingMap);
        return linkMap;
    }

    // ------------------------------------------------------------ 内部

    private record Context(Map<Integer, Landing> landings, Map<Integer, Group> groups) {}

    private Cvslink requireOwned(Integer id) {
        Cvslink link = cvslinkMapper.selectById(id);
        if (link == null) throw new IllegalArgumentException("记录不存在");
        if (!CurrentUser.canAccess(link.ownerId)) throw new ForbiddenException("无权操作该数据");
        return link;
    }

    private Context context(List<Integer> landingIds) {
        List<Integer> ids = landingIds.stream().filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) return new Context(Collections.emptyMap(), Collections.emptyMap());

        Map<Integer, Landing> landings = landingMapper.selectBatchIds(ids).stream()
                .collect(Collectors.toMap(l -> l.id, l -> l));
        List<Integer> groupIds = landings.values().stream().map(l -> l.uid)
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Integer, Group> groups = groupIds.isEmpty() ? Collections.emptyMap()
                : groupMapper.selectBatchIds(groupIds).stream().collect(Collectors.toMap(g -> g.id, g -> g));
        return new Context(landings, groups);
    }

    private Map<String, Object> landingMap(Context ctx, Integer landingId, boolean withStats) {
        Landing landing = ctx.landings().get(landingId);
        if (landing == null) return null;

        Map<String, Object> lm = new LinkedHashMap<>();
        lm.put("id", landing.id);
        lm.put("uid", landing.uid);
        lm.put("url", landing.url);
        if (withStats) {
            lm.put("neardate", landing.neardate == null ? null : landing.neardate.format(DATE_TIME));
            lm.put("cvscount", landing.cvscount);
            lm.put("total", landing.total);
        }
        Group g = ctx.groups().get(landing.uid);
        lm.put("Group", g == null ? null : Maps.pick("id", g.id, "group", g.groupName));
        return lm;
    }
}
