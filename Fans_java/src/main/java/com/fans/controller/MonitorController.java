package com.fans.controller;

import com.fans.common.Req;
import com.fans.service.MonitorService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** monitor 模块 */
@RestController
@RequestMapping("/monitor")
public class MonitorController {

    private final MonitorService service;

    public MonitorController(MonitorService service) {
        this.service = service;
    }

    /** 无返回值（空响应体） */
    @RequestMapping("/append")
    public void append(Req req) {
        service.append(req);
    }

    /** 无返回值 */
    @RequestMapping("/inctotal")
    public void incTotal(Req req) {
        service.incTotal(req);
    }

    @RequestMapping("/gtmonitor")
    public Map<String, Object> getMonitor(Req req) {
        return service.getMonitor(req);
    }

    @RequestMapping("/mthome")
    public Map<String, Object> home(Req req) {
        return service.home();
    }
}
