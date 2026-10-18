package com.fans.controller;

import com.fans.service.WechatSchemeService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序 scheme 生成，保留兼容入口。
 * 原脚本用 echo 直接输出 openlink 纯文本，这里保持一致。
 */
@RestController
public class SchemeController {

    private final WechatSchemeService service;

    public SchemeController(WechatSchemeService service) {
        this.service = service;
    }

    @RequestMapping("/wx/scheme")
    public ResponseEntity<String> scheme() {
        String link = service.generate();
        return ResponseEntity.ok()
                .contentType(new MediaType("text", "plain", java.nio.charset.StandardCharsets.UTF_8))
                .body(link == null ? "" : link);
    }
}
