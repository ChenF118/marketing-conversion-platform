package com.fans.controller;

import com.fans.common.Req;
import com.fans.service.LandingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/** landing 模块 */
@RestController
@RequestMapping("/landing")
public class LandingController {

    private final LandingService service;

    public LandingController(LandingService service) {
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

    @RequestMapping("/dellot")
    public Map<String, Object> dellot(Req req) {
        return service.deleteLot(req.ints("arr"));
    }

    @RequestMapping("/update")
    public Map<String, Object> update(Req req) {
        return service.update(req.map("data"));
    }

    /** 批量导入：data{dryRun, landingurls, group, remarks, delivery, track, open} */
    @RequestMapping("/import")
    public Map<String, Object> importLandings(Req req) {
        Map<String, Object> data = req.map("data");
        return com.fans.common.Req.truthy(data.get("dryRun"))
                ? service.importPreview(data)
                : service.importCommit(data);
    }

    @RequestMapping("/gtlanding")
    public Map<String, Object> getLanding(Req req) {
        return service.getLanding(req);
    }

    @RequestMapping("/gtl")
    public Map<String, Object> getAll(Req req) {
        return service.getLandingAll();
    }

    @RequestMapping("/tgl")
    public Map<String, Object> toggle(Req req) {
        return service.toggle(req.integer("id"), req.integer("enable"));
    }

    @RequestMapping("/lag")
    public List<Map<String, Object>> landingAndGroup(Req req) {
        return service.landingAndGroup();
    }

    @RequestMapping("/lwg")
    public Map<String, Object> landingWithGroup(Req req) {
        return service.landingWithGroup(req);
    }

    @RequestMapping("/ubd")
    public Map<String, Object> unbind(Req req) {
        return service.unbind(req.integer("id"));
    }

    @RequestMapping("/cgwx")
    public Map<String, Object> changeWeixin(Req req) {
        return service.changeWeixin(req.map("data"));
    }

    @RequestMapping("/ubl")
    public Map<String, Object> unbindLot(Req req) {
        return service.unbindLot(req.ints("arr"));
    }

    @RequestMapping("/cgl")
    public Map<String, Object> changeLot(Req req) {
        return service.changeLot(req.ints("arr"), req.map("form"));
    }

    @RequestMapping("/lar")
    public Map<String, Object> rank(Req req) {
        return service.landingRank(req);
    }

    @RequestMapping("/gtr")
    public Map<String, Object> rate(Req req) {
        return service.landingRate(req);
    }
}
