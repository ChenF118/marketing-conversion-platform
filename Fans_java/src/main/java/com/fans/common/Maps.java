package com.fans.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** 实体 -> snake_case Map（含自定义序列化器效果） */
@Component
public class Maps {

    private final ObjectMapper om;

    public Maps(ObjectMapper om) {
        this.om = om;
    }

    public Map<String, Object> of(Object entity) {
        return om.convertValue(entity, new TypeReference<LinkedHashMap<String, Object>>() {});
    }

    public List<Map<String, Object>> ofList(List<?> entities) {
        List<Map<String, Object>> out = new ArrayList<>(entities.size());
        for (Object e : entities) out.add(of(e));
        return out;
    }

    public static void without(Map<String, Object> map, String... keys) {
        for (String k : keys) map.remove(k);
    }

    public static void retain(Map<String, Object> map, String... keys) {
        List<String> keep = List.of(keys);
        map.keySet().removeIf(k -> !keep.contains(k));
    }

    public static Map<String, Object> pick(Object... kv) {
        Map<String, Object> m = new LinkedHashMap<>();
        for (int i = 0; i + 1 < kv.length; i += 2) {
            m.put(String.valueOf(kv[i]), kv[i + 1]);
        }
        return m;
    }
}
