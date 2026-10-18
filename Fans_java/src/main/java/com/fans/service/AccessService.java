package com.fans.service;

import com.fans.common.Api;
import com.fans.common.Http;
import com.fans.config.AppProperties;
import com.fans.entity.AccessToken;
import com.fans.mapper.AccessTokenMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/** 微信接口调用凭证缓存 */
@Service
public class AccessService {

    private final AccessTokenMapper accessTokenMapper;
    private final AppProperties props;
    private final Http http;

    public AccessService(AccessTokenMapper accessTokenMapper, AppProperties props, Http http) {
        this.accessTokenMapper = accessTokenMapper;
        this.props = props;
        this.http = http;
    }

    /** AccessCt::register —— 拉取 access_token 并写回 id=1 的记录 */
    @Transactional
    public Map<String, Object> refresh() {
        String token = fetchToken(props.getWechat().getAppid(), props.getWechat().getSecret());
        AccessToken row = accessTokenMapper.selectById(1);
        if (row == null) {
            row = new AccessToken();
            row.id = 1;
            row.access = token;
            accessTokenMapper.insert(row);
        } else {
            row.access = token;
            accessTokenMapper.updateById(row);
        }
        return Api.ok("成功");
    }

    public String current() {
        AccessToken row = accessTokenMapper.selectById(1);
        return row == null ? null : row.access;
    }

    public String fetchToken(String appid, String secret) {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential"
                + "&appid=" + appid + "&secret=" + secret;
        Map<String, Object> json = http.getJson(url);
        Object token = json == null ? null : json.get("access_token");
        if (token == null) {
            throw new IllegalStateException("获取微信 access_token 失败: " + json);
        }
        return String.valueOf(token);
    }
}
