package com.fans.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fans.common.Api;
import com.fans.common.CurrentUser;
import com.fans.common.ForbiddenException;
import com.fans.common.Maps;
import com.fans.common.Req;
import com.fans.entity.Wxgroup;
import com.fans.mapper.WeixinMapper;
import com.fans.mapper.WxgroupMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** 微信号分组 */
@Service
public class WxgroupService {

    private final WxgroupMapper wxgroupMapper;
    private final WeixinMapper weixinMapper;
    private final Maps maps;

    public WxgroupService(WxgroupMapper wxgroupMapper, WeixinMapper weixinMapper, Maps maps) {
        this.wxgroupMapper = wxgroupMapper;
        this.weixinMapper = weixinMapper;
        this.maps = maps;
    }

    @Transactional
    public Map<String, Object> append(Map<String, Object> data) {
        String name = Req.text(data.get("group"));
        Long exists = wxgroupMapper.selectCount(new QueryWrapper<Wxgroup>().eq("name", name));
        if (exists != null && exists > 0) {
            return Api.res(201, "该分组名称已存在！", List.of());
        }
        Wxgroup group = new Wxgroup();
        group.ownerId = CurrentUser.id();
        group.name = name;
        group.remarks = Req.text(data.get("remark"));
        wxgroupMapper.insert(group);
        return Api.ok("添加成功！");
    }

    public Map<String, Object> delete(Integer id) {
        if (id == null) return Api.ok("删除成功", 0);
        requireOwned(id);
        return Api.ok("删除成功", wxgroupMapper.deleteById(id));
    }

    @Transactional
    public Map<String, Object> update(Map<String, Object> forms) {
        Integer id = Req.toInteger(forms.get("id"));
        String name = Req.text(forms.get("group"));
        Long exists = wxgroupMapper.selectCount(new QueryWrapper<Wxgroup>()
                .like("name", name == null ? "" : name)
                .ne("id", id));
        if (exists != null && exists > 0) {
            return Api.res(201, "该分组名称已存在！", List.of());
        }
        Wxgroup group = requireOwned(id);
        group.name = name;
        group.remarks = Req.text(forms.get("remark"));
        int n = wxgroupMapper.updateById(group);
        return Api.ok("修改成功", n);
    }

    /** WxgroupCt::get_wxgroup —— withCount('Weixin') */
    public Map<String, Object> page(Req req) {
        long current = req.longVal("currentpage", 1);
        long size = req.longVal("singlepage", 10);

        Object condition = req.get("condition");
        String keyword = null;
        if (condition instanceof Map<?, ?> map) {
            Object v = map.get("name");
            if (v == null) v = map.get("words");
            if (Req.truthy(v)) keyword = String.valueOf(v);
        } else if (Req.truthy(condition)) {
            keyword = String.valueOf(condition);
        }

        QueryWrapper<Wxgroup> wrapper = new QueryWrapper<>();
        if (keyword != null) wrapper.like("name", keyword);
        if (!CurrentUser.isAdmin()) wrapper.eq("ownerId", CurrentUser.id());

        long total = wxgroupMapper.selectCount(wrapper);
        Page<Wxgroup> page = wxgroupMapper.selectPage(new Page<>(current, size), wrapper.orderByAsc("id"));

        List<Map<String, Object>> rows = maps.ofList(page.getRecords());
        List<Integer> ids = rows.stream().map(r -> Req.toInteger(r.get("id")))
                .filter(java.util.Objects::nonNull).collect(Collectors.toList());
        Map<Integer, Long> counts = new HashMap<>();
        if (!ids.isEmpty()) {
            for (Map<String, Object> m : weixinMapper.countByGroupIds(ids)) {
                counts.put(Req.toInteger(m.get("uid")), ((Number) m.get("cnt")).longValue());
            }
        }
        rows.forEach(r -> r.put("weixin_count", counts.getOrDefault(Req.toInteger(r.get("id")), 0L)));
        return Api.page(rows, total);
    }

    public Map<String, Object> list() {
        QueryWrapper<Wxgroup> wrapper = new QueryWrapper<Wxgroup>().orderByAsc("id");
        if (!CurrentUser.isAdmin()) wrapper.eq("ownerId", CurrentUser.id());
        List<Wxgroup> groups = wxgroupMapper.selectList(wrapper);
        List<Map<String, Object>> rows = maps.ofList(groups);
        rows.forEach(m -> Maps.without(m, "remarks"));
        return Api.ok("成功", rows);
    }

    private Wxgroup requireOwned(Integer id) {
        Wxgroup group = wxgroupMapper.selectById(id);
        if (group == null) throw new IllegalArgumentException("记录不存在");
        if (!CurrentUser.canAccess(group.ownerId)) throw new ForbiddenException("无权操作该数据");
        return group;
    }
}
