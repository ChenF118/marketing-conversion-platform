package com.fans.controller;

import com.fans.common.Req;
import com.fans.service.TencentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** tencent 模块 */
@RestController
@RequestMapping("/tencent")
public class TencentController {

    private final TencentService service;

    public TencentController(TencentService service) {
        this.service = service;
    }

    @RequestMapping("/tfeed")
    public String tFeedback(Req req) {
        return service.tFeedback(req);
    }

    @RequestMapping("/tfeeds")
    public Map<String, Object> tFeedbacks(Req req) {
        return service.tFeedbacks(req);
    }
}
