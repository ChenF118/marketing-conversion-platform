package com.fans.common;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 统一响应体：
 *   ressend($status, $message, $data)      -> {code, message, data}
 *   json(['code'=>200,'msg'=>..., 'data'=>..., 'count'=>...]) -> {code, msg, data, count}
 */
public final class Api {

    private Api() {}

    public static Map<String, Object> res(int code, String message, Object data) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("code", code);
        m.put("message", message);
        m.put("data", data == null ? List.of() : data);
        return m;
    }

    public static Map<String, Object> ok(String message) {
        return res(200, message, List.of());
    }

    public static Map<String, Object> ok(String message, Object data) {
        return res(200, message, data);
    }

    /** 列表接口使用 {code, msg, data, count} 结构 */
    public static Map<String, Object> page(Object data, long count) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("code", 200);
        m.put("msg", "获取成功！");
        m.put("data", data);
        m.put("count", count);
        return m;
    }

    /** cvstype 列表使用 {code, msg, data} 结构（无 count） */
    public static Map<String, Object> msgData(Object data) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("code", 200);
        m.put("msg", "获取成功！");
        m.put("data", data);
        return m;
    }

    /** getgroup / groupsearch / get_wxgroup 等使用 {code,message,data:{data,count}} 结构 */
    public static Map<String, Object> nestedPage(Object data, long count) {
        Map<String, Object> inner = new LinkedHashMap<>();
        inner.put("data", data);
        inner.put("count", count);
        return res(200, "成功", inner);
    }
}
