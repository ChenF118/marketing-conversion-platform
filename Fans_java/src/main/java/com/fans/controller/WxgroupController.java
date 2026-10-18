package com.fans.controller;

import com.fans.common.Req;
import com.fans.service.WxgroupService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** wxgroup 模块 */
@RestController
@RequestMapping("/wxgroup")
public class WxgroupController {

    private final WxgroupService service;

    public WxgroupController(WxgroupService service) {
        this.service = service;
    }

    @RequestMapping("/append")
    public Map<String, Object> append(Req req) {
        return service.append(req.map("data"));
    }

    @RequestMapping("/del")
    public Map<String, Object> del(Req req) {
        return service.delete(req.integer("id"));
    }

    @RequestMapping("/update")
    public Map<String, Object> update(Req req) {
        return service.update(req.map("forms"));
    }

    @RequestMapping("/gtg")
    public Map<String, Object> getGroup(Req req) {
        return service.page(req);
    }

    @RequestMapping("/ggl")
    public Map<String, Object> groupList(Req req) {
        return service.list();
    }
}
