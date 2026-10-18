package com.fans.controller;

import com.fans.common.Req;
import com.fans.service.AdminService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** admin 模块 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    @RequestMapping("/rgs")
    public Map<String, Object> register(Req req) {
        return service.register(req.map("form"));
    }

    @RequestMapping("/login")
    public Map<String, Object> login(Req req) {
        return service.login(req.map("form"));
    }

    @RequestMapping("/profile")
    public Map<String, Object> profile(Req req) {
        return service.profile(req.integer("id"));
    }

    @RequestMapping("/reset")
    public Map<String, Object> reset(Req req) {
        return service.changePassword(req.map("form"));
    }
}
