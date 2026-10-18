package com.fans.controller;

import com.fans.common.Req;
import com.fans.service.GroupService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** group 模块 */
@RestController
@RequestMapping("/group")
public class GroupController {

    private final GroupService service;

    public GroupController(GroupService service) {
        this.service = service;
    }

    @RequestMapping("/append")
    public Map<String, Object> append(Req req) {
        return service.append(req.map("forms"));
    }

    @RequestMapping("/del")
    public Map<String, Object> del(Req req) {
        return service.delete(req.integer("id"));
    }

    @RequestMapping("/update")
    public Map<String, Object> update(Req req) {
        return service.update(req.map("forms"));
    }

    @RequestMapping("/gtgroup")
    public Map<String, Object> getGroup(Req req) {
        return service.page(req);
    }

    @RequestMapping("/gta")
    public Map<String, Object> getAll(Req req) {
        return service.all();
    }

    @RequestMapping("/gts")
    public Map<String, Object> search(Req req) {
        return service.search(req.str("words"));
    }
}
