package com.fans.common;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HTTP 客户端封装（连接 3s / 读取 10s 超时）。
 */
@Component
public class Http {

    private final RestTemplate rest;

    public Http() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(10000);
        this.rest = new RestTemplate(factory);
    }

    public String get(String url) {
        return rest.getForObject(url, String.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getJson(String url) {
        return rest.getForObject(url, Map.class);
    }

    public Map<String, Object> postJson(String url, Object body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<Map> resp = rest.exchange(url, HttpMethod.POST, new HttpEntity<>(body, headers), Map.class);
        return resp.getBody() == null ? new LinkedHashMap<>() : (Map<String, Object>) resp.getBody();
    }

    public Resp postForm(String url, MultiValueMap<String, String> form) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ResponseEntity<String> resp = rest.exchange(url, HttpMethod.POST, new HttpEntity<>(form, headers), String.class);
        return new Resp(resp.getStatusCode().value(), resp.getStatusCode().toString(), resp.getBody());
    }

    public static MultiValueMap<String, String> form() {
        return new LinkedMultiValueMap<>();
    }

    public record Resp(int code, String reason, String body) {}
}
