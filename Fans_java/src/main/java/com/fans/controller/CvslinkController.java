package com.fans.controller;

import com.fans.common.Req;
import com.fans.service.CvslinkService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** cvslink 模块 */
@RestController
@RequestMapping("/cvslink")
public class CvslinkController {

    private final CvslinkService service;

    public CvslinkController(CvslinkService service) {
        this.service = service;
    }

    @RequestMapping("/append")
    public Map<String, Object> append(Req req) {
        return service.append(req.map("data"));
    }

    @RequestMapping("/del")
    public Map<String, Object> del(Req req) {
        return service.delete(req);
    }

    @RequestMapping("/update")
    public Map<String, Object> update(Req req) {
        return service.update(req.map("data"));
    }

    @RequestMapping("/min")
    public Map<String, Object> min(Req req) {
        return service.minSecond(req.map("data"));
    }

    @RequestMapping("/gbl")
    public Map<String, Object> getBelongs(Req req) {
        return service.getBelongs(req);
    }

    @RequestMapping("/gtr")
    public Map<String, Object> getRate(Req req) {
        return service.getCvsRate(req);
    }

    @RequestMapping("/flg")
    public Object forLandingGet(Req req) {
        return service.forLandingGet(req);
    }
}
