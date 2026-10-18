package com.fans.service;

import com.fans.common.Http;
import com.fans.config.AppProperties;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 小程序 scheme 生成：
 * 用小程序 appid 换取 access_token，再调用 wxa/generatescheme 生成打开小程序链接。
 */
@Service
public class WechatSchemeService {

    private final AppProperties props;
    private final AccessService accessService;
    private final Http http;

    public WechatSchemeService(AppProperties props, AccessService accessService, Http http) {
        this.props = props;
        this.accessService = accessService;
        this.http = http;
    }

    public String generate() {
        String token = accessService.fetchToken(props.getWechat().getSchemeAppid(), props.getWechat().getSchemeSecret());
        String url = "https://api.weixin.qq.com/wxa/generatescheme?access_token=" + token;

        Map<String, Object> jump = new LinkedHashMap<>();
        jump.put("path", "/pages/index/index");
        jump.put("query", "id=19");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("jump_wxa", jump);
        body.put("is_expire", true);
        body.put("expire_time", 1656777600L);

        Map<String, Object> resp = http.postJson(url, body);
        Object openlink = resp == null ? null : resp.get("openlink");
        return openlink == null ? null : String.valueOf(openlink);
    }
}
