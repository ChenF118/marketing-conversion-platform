package com.fans.service;

import com.fans.common.Http;
import com.fans.common.Req;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/** 腾讯广告转化回传 */
@Service
public class TencentService {

    private final Http http;

    public TencentService(Http http) {
        this.http = http;
    }

    /** GET 上报（tfeedback） */
    public String tFeedback(Req req) {
        String clickid = req.str("clickid");
        String actiontime = req.str("cvstime");
        String link = URLEncoder.encode(req.str("link") == null ? "" : req.str("link"), StandardCharsets.UTF_8);
        String url = "http://tracking.e.qq.com/conv/web?clickid=" + clickid
                + "&action_time=" + actiontime
                + "&action_type=ACTIVATE_APP"
                + "&link=" + link;
        return http.get(url);
    }

    /** 自归因上报（tfeedbacks），POST 表单 */
    public Map<String, Object> tFeedbacks(Req req) {
        String cb = "YWRzX3NlcnZpY2UsMTU4NDUxMDI3OSwyNjg5MzNhMzc5MTM0YzBjMDQ4ZGZjMGQyNGYzMTk0NWYzMzJiOWNi&conv_id=10001";
        String url = "http://tracking.e.qq.com/conv?cb=" + cb;

        MultiValueMap<String, String> form = Http.form();
        form.add("actions[action_time]", "1492998081");
        form.add("actions[action_type]", "ACTIVATE_APP");
        form.add("actions[action_param][clickid]", nullSafe(req.str("click_id")));
        form.add("actions[action_param][click_time]", nullSafe(req.str("click_time")));
        form.add("actions[action_param][account_id]", nullSafe(req.str("account_id")));
        form.add("actions[action_param][device_os_type]", nullSafe(req.str("device_os_type")));
        form.add("actions[action_param][promoted_object_id]", nullSafe(req.str("promoted_object_id")));
        form.add("actions[action_param][qz_gdt]", nullSafe(req.str("qz_gdt")));

        Http.Resp resp = http.postForm(url, form);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", resp.code());
        body.put("reason", resp.reason());
        body.put("body", resp.body());
        return body;
    }

    private static String nullSafe(String v) {
        return v == null ? "" : v;
    }
}
