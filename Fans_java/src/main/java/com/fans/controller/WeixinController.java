package com.fans.controller;

import com.fans.common.Req;
import com.fans.service.WeixinService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** weixin 模块 */
@RestController
@RequestMapping("/weixin")
public class WeixinController {

    private final WeixinService service;

    public WeixinController(WeixinService service) {
        this.service = service;
    }

    @RequestMapping("/getwx")
    public Map<String, Object> getWeixin(Req req) {
        return service.getWeixin(req);
    }

    @RequestMapping("/ctwx")
    public Map<String, Object> cotrWeixin(Req req) {
        return service.cotrWeixin(req);
    }

    @RequestMapping("/udwx")
    public Map<String, Object> update(Req req) {
        return service.update(req.map("data"));
    }

    @RequestMapping("/delwx")
    public Map<String, Object> delete(Req req) {
        return service.delete(req.ints("arr"));
    }

    @RequestMapping("/append")
    public Map<String, Object> append(Req req) {
        return service.append(req.map("data"));
    }

    @RequestMapping("/udqr")
    public Map<String, Object> updateQr(Req req) {
        return service.updateQr(req.map("data"));
    }

    @RequestMapping("/delqr")
    public Map<String, Object> deleteQr(Req req) {
        return service.deleteQr(req.integer("id"));
    }

    @RequestMapping("/cjol")
    public Map<String, Object> changeOnline(Req req) {
        return service.changeOnline(req.integer("id"), req.integer("online"));
    }

    @RequestMapping("/cjlv")
    public Map<String, Object> changeLevel(Req req) {
        return service.changeLevel(req.integer("id"), req.integer("level"));
    }
}
