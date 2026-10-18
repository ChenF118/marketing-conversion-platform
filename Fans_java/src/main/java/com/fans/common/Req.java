package com.fans.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一参数解析（query + JSON body）：
 *  - GET/POST 表单参数（含 a[b]、a[] 形式的方括号参数）
 *  - application/json 请求体（axios 默认）
 *  - 两者合并，body 优先
 */
public final class Req {

    private final Map<String, Object> params;

    private Req(Map<String, Object> params) {
        this.params = params;
    }

    public static Req of(HttpServletRequest request, ObjectMapper om) {
        Map<String, Object> root = new LinkedHashMap<>();

        // 1) query string + form body（Servlet 已按 UTF-8 解码）
        for (Map.Entry<String, String[]> e : request.getParameterMap().entrySet()) {
            putKey(root, e.getKey(), e.getValue());
        }

        // 2) JSON body
        String contentType = request.getContentType();
        if (contentType != null && contentType.toLowerCase().startsWith("application/json")) {
            try (InputStream in = request.getInputStream()) {
                byte[] bytes = in.readAllBytes();
                if (bytes.length > 0) {
                    Map<String, Object> body = om.readValue(bytes, new TypeReference<Map<String, Object>>() {});
                    deepMerge(root, body);
                }
            } catch (IOException ex) {
                throw new IllegalArgumentException("请求体解析失败: " + ex.getMessage(), ex);
            }
        }
        return new Req(root);
    }

    // ---------------------------------------------------------------- 取值

    public boolean has(String key) {
        return params.containsKey(key);
    }

    public Object get(String key) {
        return params.get(key);
    }

    public Map<String, Object> raw() {
        return params;
    }

    public String str(String key) {
        return text(params.get(key));
    }

    public Integer integer(String key) {
        return toInteger(params.get(key));
    }

    public int intVal(String key, int def) {
        Integer v = integer(key);
        return v == null ? def : v;
    }

    public long longVal(String key, long def) {
        Integer v = integer(key);
        return v == null ? def : v.longValue();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> map(String key) {
        Object v = params.get(key);
        if (v instanceof Map) {
            return (Map<String, Object>) v;
        }
        return new LinkedHashMap<>();
    }

    public List<Object> list(String key) {
        Object v = params.get(key);
        List<Object> out = new ArrayList<>();
        if (v instanceof Collection) {
            out.addAll((Collection<?>) v);
        } else if (v instanceof Object[] arr) {
            for (Object o : arr) out.add(o);
        } else if (v != null) {
            out.add(v);
        }
        return out;
    }

    /** 兼容 arr[]=1&arr[]=2 / arr: [1,2] / arr: "1" */
    public List<Integer> ints(String key) {
        List<Integer> out = new ArrayList<>();
        for (Object o : list(key)) {
            Integer i = toInteger(o);
            if (i != null) out.add(i);
        }
        return out;
    }

    /** 真值判断：null / 空串 / "0" / 0 / 空集合 为 false */
    public boolean truthy(String key) {
        return truthy(params.get(key));
    }

    public static boolean truthy(Object v) {
        if (v == null) return false;
        if (v instanceof Boolean b) return b;
        if (v instanceof Number n) return n.doubleValue() != 0;
        if (v instanceof CharSequence s) return s.length() > 0 && !"0".equals(s.toString());
        if (v instanceof Collection<?> c) return !c.isEmpty();
        if (v instanceof Map<?, ?> m) return !m.isEmpty();
        return true;
    }

    // ---------------------------------------------------------------- 转换

    public static String text(Object v) {
        if (v == null) return null;
        if (v instanceof String s) return s;
        if (v instanceof Double d && d == Math.rint(d)) return String.valueOf(d.longValue());
        return String.valueOf(v);
    }

    public static Integer toInteger(Object v) {
        if (v == null) return null;
        if (v instanceof Number n) return n.intValue();
        if (v instanceof Boolean b) return b ? 1 : 0;
        String s = String.valueOf(v).trim();
        if (s.isEmpty() || "null".equalsIgnoreCase(s) || "undefined".equalsIgnoreCase(s)) return null;
        try {
            return (int) Double.parseDouble(s);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    // ---------------------------------------------------------------- 解析

    static void putKey(Map<String, Object> root, String key, String[] values) {
        if (key == null) return;
        if (key.endsWith("[]")) {
            String base = key.substring(0, key.length() - 2);
            Map<String, Object> parent = ensureParent(root, base);
            String name = lastSegment(base);
            Object cur = parent.get(name);
            List<Object> list;
            if (cur instanceof List<?> existing) {
                @SuppressWarnings("unchecked")
                List<Object> cast = (List<Object>) existing;
                list = cast;
            } else {
                list = new ArrayList<>();
                parent.put(name, list);
            }
            for (String v : values) list.add(v);
            return;
        }

        Map<String, Object> parent = ensureParent(root, key);
        String name = lastSegment(key);
        if (values.length == 1) {
            Object cur = parent.get(name);
            if (cur == null) {
                parent.put(name, values[0]);
            } else if (cur instanceof List<?> existing) {
                @SuppressWarnings("unchecked")
                List<Object> cast = (List<Object>) existing;
                cast.add(values[0]);
            } else {
                List<Object> list = new ArrayList<>();
                list.add(cur);
                list.add(values[0]);
                parent.put(name, list);
            }
        } else {
            List<Object> list = new ArrayList<>();
            for (String v : values) list.add(v);
            parent.put(name, list);
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> ensureParent(Map<String, Object> root, String key) {
        List<String> segs = segments(key);
        Map<String, Object> cur = root;
        for (int i = 0; i < segs.size() - 1; i++) {
            String seg = segs.get(i);
            if (seg.isEmpty()) continue;
            Object next = cur.get(seg);
            if (!(next instanceof Map)) {
                next = new LinkedHashMap<String, Object>();
                cur.put(seg, next);
            }
            cur = (Map<String, Object>) next;
        }
        return cur;
    }

    private static String lastSegment(String key) {
        List<String> segs = segments(key);
        return segs.isEmpty() ? key : segs.get(segs.size() - 1);
    }

    /** "condition[group]" -> ["condition","group"]；"id" -> ["id"] */
    static List<String> segments(String key) {
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (c == '[') {
                if (cur.length() > 0) {
                    out.add(cur.toString());
                    cur.setLength(0);
                }
            } else if (c == ']') {
                out.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        if (cur.length() > 0) out.add(cur.toString());
        return out;
    }

    @SuppressWarnings("unchecked")
    static void deepMerge(Map<String, Object> target, Map<String, Object> source) {
        for (Map.Entry<String, Object> e : source.entrySet()) {
            Object sv = e.getValue();
            Object tv = target.get(e.getKey());
            if (sv instanceof Map && tv instanceof Map) {
                deepMerge((Map<String, Object>) tv, (Map<String, Object>) sv);
            } else {
                target.put(e.getKey(), sv);
            }
        }
    }
}
