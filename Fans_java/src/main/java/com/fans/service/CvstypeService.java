package com.fans.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fans.common.Api;
import com.fans.common.Maps;
import com.fans.common.Req;
import com.fans.entity.Cvstype;
import com.fans.mapper.CvstypeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/** 行为类型 */
@Service
public class CvstypeService {

    private final CvstypeMapper cvstypeMapper;
    private final Maps maps;

    public CvstypeService(CvstypeMapper cvstypeMapper, Maps maps) {
        this.cvstypeMapper = cvstypeMapper;
        this.maps = maps;
    }

    @Transactional
    public Map<String, Object> append(Map<String, Object> data) {
        Integer typeint = Req.toInteger(data.get("int"));
        Long exists = cvstypeMapper.selectCount(new QueryWrapper<Cvstype>().eq("typeint", typeint));
        if (exists != null && exists > 0) {
            return Api.res(201, "该埋点已存在！请编辑", List.of());
        }
        Cvstype type = new Cvstype();
        type.typename = Req.text(data.get("type"));
        type.typeint = typeint;
        cvstypeMapper.insert(type);
        return Api.ok("添加成功！");
    }

    public Map<String, Object> delete(Integer id) {
        int n = id == null ? 0 : cvstypeMapper.deleteById(id);
        return Api.ok("删除成功", n);
    }

    @Transactional
    public Map<String, Object> updateName(Req req) {
        Integer id = req.integer("id");
        Cvstype type = cvstypeMapper.selectById(id);
        if (type == null) return Api.res(201, "记录不存在！", List.of());
        type.typename = req.str("typename");
        cvstypeMapper.updateById(type);
        return Api.ok("修改成功！");
    }

    public Map<String, Object> getTypeList() {
        List<Cvstype> list = cvstypeMapper.selectList(new QueryWrapper<Cvstype>().orderByAsc("id"));
        return Api.msgData(maps.ofList(list));
    }

    public Map<String, Object> typeList() {
        List<Cvstype> list = cvstypeMapper.selectList(
                new QueryWrapper<Cvstype>().select("typename", "typeint").orderByAsc("id"));
        List<Map<String, Object>> rows = maps.ofList(list);
        rows.forEach(m -> Maps.retain(m, "typename", "typeint"));
        return Api.msgData(rows);
    }
}
