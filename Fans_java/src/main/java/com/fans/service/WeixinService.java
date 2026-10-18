package com.fans.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fans.common.Api;
import com.fans.common.CurrentUser;
import com.fans.common.ForbiddenException;
import com.fans.common.Maps;
import com.fans.common.Req;
import com.fans.entity.Weixin;
import com.fans.entity.Wxgroup;
import com.fans.mapper.WeixinMapper;
import com.fans.mapper.WxgroupMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/** 微信号 */
@Service
public class WeixinService {

    private static final Set<String> ORDER_COLUMNS =
            Set.of("visits", "copyCount", "report", "level", "online", "id", "createTime");

    private final WeixinMapper weixinMapper;
    private final WxgroupMapper wxgroupMapper;
    private final Maps maps;

    public WeixinService(WeixinMapper weixinMapper, WxgroupMapper wxgroupMapper, Maps maps) {
        this.weixinMapper = weixinMapper;
        this.wxgroupMapper = wxgroupMapper;
        this.maps = maps;
    }

    @Transactional
    public Map<String, Object> append(Map<String, Object> data) {
        String wxh = Req.text(data.get("wxh"));
        Long exists = weixinMapper.selectCount(new QueryWrapper<Weixin>().eq("wxh", wxh));
        if (exists != null && exists > 0) {
            return Api.res(201, "该微信号已存在！", List.of());
        }
        Weixin weixin = new Weixin();
        weixin.ownerId = CurrentUser.id();
        weixin.wxh = wxh;
        weixin.wxname = Req.text(data.get("wxname"));
        weixin.erweima = Req.text(data.get("erweima"));
        weixin.sex = Req.text(data.get("sex"));
        weixin.uid = Req.toInteger(data.get("wxgroup"));
        weixin.online = Req.toInteger(data.get("online"));
        weixin.control = Req.toInteger(data.get("auto"));
        weixin.level = Req.toInteger(data.get("level"));
        weixin.remarks = Req.text(data.get("remarks"));
        weixinMapper.insert(weixin);
        return Api.res(200, "成功", Boolean.TRUE);
    }

    /** 微信号统计：仅按 words 过滤（表中无 url/cvsdate 列） */
    public Map<String, Object> getWeixin(Req req) {
        Map<String, Object> filter = req.map("condition");
        long current = req.longVal("currentpage", 1);
        long size = req.longVal("singlepage", 10);

        QueryWrapper<Weixin> wrapper = new QueryWrapper<Weixin>()
                .select("wxh", "visits", "copyCount", "report")
                .orderByAsc("id");
        if (!CurrentUser.isAdmin()) wrapper.eq("ownerId", CurrentUser.id());
        if (Req.truthy(filter.get("words"))) wrapper.like("wxh", Req.text(filter.get("words")));

        QueryWrapper<Weixin> countWrapper = new QueryWrapper<Weixin>()
                .like(Req.truthy(filter.get("words")), "wxh", Req.text(filter.get("words")));
        if (!CurrentUser.isAdmin()) countWrapper.eq("ownerId", CurrentUser.id());

        long total = weixinMapper.selectCount(countWrapper);

        wrapper.last("LIMIT " + Math.max(0, (current - 1) * size) + ", " + size);
        List<Map<String, Object>> rows = maps.ofList(weixinMapper.selectList(wrapper));
        rows.forEach(m -> Maps.retain(m, "wxh", "visits", "copy", "report"));
        return Api.page(rows, total);
    }

    /** WeixinCt::cotrweixin —— hasWhere('Wxgroup') + with('Wxgroup') */
    public Map<String, Object> cotrWeixin(Req req) {
        Map<String, Object> filter = req.map("condition");
        long current = req.longVal("currentpage", 1);
        long size = req.longVal("singlepage", 10);

        Map<String, Object> q = new HashMap<>();
        if (Req.truthy(filter.get("group2"))) q.put("groupName", Req.text(filter.get("group2")));
        if (Req.truthy(filter.get("words"))) q.put("wxhLike", Req.text(filter.get("words")));

        String group3 = Req.text(filter.get("group3"));
        if ("0".equals(group3)) q.put("online", 0);
        else if ("1".equals(group3)) q.put("online", 1);
        else if ("2".equals(group3)) q.put("erweimaNull", true);
        else if ("3".equals(group3)) q.put("erweimaNotEmpty", true);

        String order = Req.text(filter.get("group1"));
        if ("copy".equals(order)) order = "copyCount"; // 兼容前端旧字段名
        if (order != null && ORDER_COLUMNS.contains(order)) {
            q.put("orderClause", "w.`" + order + "` DESC");
        }
        if (!CurrentUser.isAdmin()) q.put("ownerId", CurrentUser.id());

        List<Weixin> list = weixinMapper.selectCotrPage(q, Math.max(0, (current - 1) * size), size);
        long total = weixinMapper.countCotr(q);

        List<Map<String, Object>> rows = maps.ofList(list);
        attachWxgroups(rows);
        return Api.page(rows, total);
    }

    @Transactional
    public Map<String, Object> updateQr(Map<String, Object> data) {
        Integer id = Req.toInteger(data.get("id"));
        Weixin weixin = requireOwned(id);
        weixin.erweima = Req.text(data.get("sendimg"));
        weixinMapper.updateById(weixin);
        return Api.ok("成功");
    }

    @Transactional
    public Map<String, Object> deleteQr(Integer id) {
        Weixin weixin = requireOwned(id);
        weixin.erweima = "";
        weixinMapper.updateById(weixin);
        return Api.ok("成功");
    }

    @Transactional
    public Map<String, Object> update(Map<String, Object> data) {
        Integer id = Req.toInteger(data.get("id"));
        Weixin weixin = requireOwned(id);
        weixin.wxh = Req.text(data.get("wxh"));
        weixin.wxname = Req.text(data.get("wxname"));
        weixin.erweima = Req.text(data.get("erweima"));
        weixin.sex = Req.text(data.get("sex"));
        weixin.uid = Req.toInteger(data.get("wxgroup"));
        weixin.online = Req.toInteger(data.get("online"));
        weixin.control = Req.toInteger(data.get("auto"));
        weixin.level = Req.toInteger(data.get("level"));
        weixin.remarks = Req.text(data.get("remarks"));
        int n = weixinMapper.updateById(weixin);
        return Api.ok("修改成功", n);
    }

    public Map<String, Object> delete(List<Integer> ids) {
        int n = 0;
        for (Integer id : ids) {
            Weixin w = weixinMapper.selectById(id);
            if (w != null && CurrentUser.canAccess(w.ownerId)) n += weixinMapper.deleteById(id);
        }
        return Api.ok("删除成功", n);
    }

    @Transactional
    public Map<String, Object> changeOnline(Integer id, Integer online) {
        Weixin weixin = requireOwned(id);
        weixin.online = online;
        weixinMapper.updateById(weixin);
        return Api.ok("成功");
    }

    @Transactional
    public Map<String, Object> changeLevel(Integer id, Integer level) {
        Weixin weixin = requireOwned(id);
        weixin.level = level;
        weixinMapper.updateById(weixin);
        return Api.ok("成功");
    }

    private Weixin requireOwned(Integer id) {
        Weixin weixin = weixinMapper.selectById(id);
        if (weixin == null) throw new IllegalArgumentException("记录不存在");
        if (!CurrentUser.canAccess(weixin.ownerId)) throw new ForbiddenException("无权操作该数据");
        return weixin;
    }

    private void attachWxgroups(List<Map<String, Object>> rows) {
        List<Integer> ids = rows.stream().map(r -> Req.toInteger(r.get("uid")))
                .filter(Objects::nonNull).distinct().collect(Collectors.toList());
        Map<Integer, Wxgroup> groups = ids.isEmpty() ? Collections.emptyMap()
                : wxgroupMapper.selectBatchIds(ids).stream().collect(Collectors.toMap(g -> g.id, g -> g));
        for (Map<String, Object> row : rows) {
            Wxgroup g = groups.get(Req.toInteger(row.get("uid")));
            row.put("Wxgroup", g == null ? null : Maps.pick("id", g.id, "name", g.name));
        }
    }
}
