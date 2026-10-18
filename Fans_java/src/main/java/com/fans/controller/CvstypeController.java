package com.fans.controller;

import com.fans.common.Req;
import com.fans.service.CvstypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/** cvstype 模块 */
@RestController
@RequestMapping("/cvstype")
public class CvstypeController {

    private final CvstypeService service;

    public CvstypeController(CvstypeService service) {
        this.service = service;
    }

    @RequestMapping("/gtl")
    public Map<String, Object> getTypeList(Req req) {
        return service.getTypeList();
    }

    @RequestMapping("/upn")
    public Map<String, Object> updateName(Req req) {
        return service.updateName(req);
    }

    @RequestMapping("/del")
    public Map<String, Object> del(Req req) {
        return service.delete(req.integer("id"));
    }

    @RequestMapping("/append")
    public Map<String, Object> append(Req req) {
        return service.append(req.map("data"));
    }

    @RequestMapping("/tpl")
    public Map<String, Object> typeList(Req req) {
        return service.typeList();
    }
}
