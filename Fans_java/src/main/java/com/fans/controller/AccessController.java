package com.fans.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fans.common.Req;
import com.fans.service.AccessService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** access 模块 */
@RestController
@RequestMapping("/access")
public class AccessController {

    private final AccessService service;
    private final ObjectMapper om;

    public AccessController(AccessService service, ObjectMapper om) {
        this.service = service;
        this.om = om;
    }

    @RequestMapping("/index")
    public String index() {
        return "user_Admin";
    }

    @RequestMapping("/add")
    public Map<String, Object> add(Req req) {
        return service.refresh();
    }

    /** 返回带引号的 JSON 字符串 */
    @RequestMapping("/get")
    public ResponseEntity<String> get(Req req) throws Exception {
        String value = service.current();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(om.writeValueAsString(value));
    }
}
