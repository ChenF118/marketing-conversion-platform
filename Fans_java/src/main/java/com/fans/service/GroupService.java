package com.fans.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fans.common.Api;
import com.fans.common.CurrentUser;
import com.fans.common.ForbiddenException;
import com.fans.common.Maps;
import com.fans.common.Req;
import com.fans.entity.Group;
import com.fans.mapper.GroupMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** 落地页分组 */
@Service
public class GroupService {

    private final GroupMapper groupMapper;
    private final Maps maps;

    public GroupService(GroupMapper groupMapper, Maps maps) {
        this.groupMapper = groupMapper;
        this.maps = maps;
    }

    @Transactional
    public Map<String, Object> append(Map<String, Object> forms) {
        String name = Req.text(forms.get("group"));
        QueryWrapper<Group> exists = new QueryWrapper<Group>().like("groupName", name == null ? "" : name);
        if (groupMapper.selectCount(exists) > 0) {
            return Api.res(201, "该分组名称已存在！", List.of());
        }
        Group group = new Group();
        group.ownerId = CurrentUser.id();
        group.groupName = name;
        group.remarks = Req.text(forms.get("remarks"));
        groupMapper.insert(group);
        return Api.ok("添加成功！");
    }

    public Map<String, Object> delete(Integer id) {
        if (id == null) return Api.ok("删除成功！", 0);
        requireOwned(id);
        return Api.ok("删除成功！", groupMapper.deleteById(id));
    }

    @Transactional
    public Map<String, Object> update(Map<String, Object> forms) {
        Integer id = Req.toInteger(forms.get("id"));
        Group group = requireOwned(id);
        group.groupName = Req.text(forms.get("group"));
        group.remarks = Req.text(forms.get("remarks"));
        int n = groupMapper.updateById(group);
        return Api.ok("修改成功", n);
    }

    /** GroupCt::getgroup —— withCount('Landing') */
    public Map<String, Object> page(Req req) {
        long current = req.longVal("currentpage", 1);
        long size = req.longVal("singlepage", 10);

        Page<Group> page = groupMapper.selectPage(new Page<>(current, size),
                ownerWrapper(new QueryWrapper<Group>()).orderByAsc("id"));
        long total = groupMapper.selectCount(ownerWrapper(new QueryWrapper<>()));

        List<Map<String, Object>> rows = maps.ofList(page.getRecords());
        attachLandingCount(rows);
        return Api.nestedPage(rows, total);
    }

    public Map<String, Object> all() {
        List<Group> groups = groupMapper.selectList(ownerWrapper(new QueryWrapper<Group>()).orderByAsc("id"));
        List<Map<String, Object>> rows = maps.ofList(groups);
        rows.forEach(m -> Maps.without(m, "remarks", "total"));
        return Api.ok("成功", rows);
    }

    /** 保持既有行为：搜索分页固定第 1 页 / 10 条 */
    public Map<String, Object> search(String words) {
        QueryWrapper<Group> wrapper = ownerWrapper(new QueryWrapper<Group>().like("groupName", words == null ? "" : words));
        long total = groupMapper.selectCount(wrapper);
        Page<Group> page = groupMapper.selectPage(new Page<>(1, 10), wrapper.orderByAsc("id"));
        List<Map<String, Object>> rows = maps.ofList(page.getRecords());
        rows.forEach(m -> Maps.without(m, "remarks", "total"));
        return Api.nestedPage(rows, total);
    }

    private QueryWrapper<Group> ownerWrapper(QueryWrapper<Group> wrapper) {
        if (!CurrentUser.isAdmin()) wrapper.eq("ownerId", CurrentUser.id());
        return wrapper;
    }

    private Group requireOwned(Integer id) {
        Group group = groupMapper.selectById(id);
        if (group == null) throw new IllegalArgumentException("记录不存在");
        if (!CurrentUser.canAccess(group.ownerId)) throw new ForbiddenException("无权操作该数据");
        return group;
    }

    private void attachLandingCount(List<Map<String, Object>> rows) {
        List<Integer> ids = rows.stream().map(r -> Req.toInteger(r.get("id")))
                .filter(java.util.Objects::nonNull).collect(Collectors.toList());
        Map<Integer, Long> counts = new HashMap<>();
        if (!ids.isEmpty()) {
            for (Map<String, Object> m : groupMapper.countLandingByUids(ids)) {
                counts.put(Req.toInteger(m.get("uid")),
                        ((Number) m.get("cnt")).longValue());
            }
        }
        List<Map<String, Object>> copy = new ArrayList<>(rows);
        for (Map<String, Object> row : copy) {
            row.put("landing_count", counts.getOrDefault(Req.toInteger(row.get("id")), 0L));
        }
    }
}
