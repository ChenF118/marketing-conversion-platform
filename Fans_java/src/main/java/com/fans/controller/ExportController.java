package com.fans.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fans.common.CurrentUser;
import com.fans.entity.Landing;
import com.fans.entity.Monitor;
import com.fans.entity.Weixin;
import com.fans.mapper.LandingMapper;
import com.fans.mapper.MonitorMapper;
import com.fans.mapper.WeixinMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** 数据导出（UTF-8 BOM，Excel 可直接打开） */
@RestController
public class ExportController {

    private static final String[] TYPE_TEXT = {
            "", "点击", "长按识别", "微信复制按钮点击", "微信打开按钮点击", "咨询按钮点击",
            "下载按钮点击", "购买按钮点击", "注册按钮点击", "登录按钮点击", "拨打电话按钮点击",
            "短信按钮点击", "加入购物车按钮点击", "地图按钮点击", "QQ咨询按钮点击", "抽奖按钮点击",
            "投票按钮点击", "表单确认提交按钮点击"
    };

    private final LandingMapper landingMapper;
    private final WeixinMapper weixinMapper;
    private final MonitorMapper monitorMapper;

    public ExportController(LandingMapper landingMapper, WeixinMapper weixinMapper, MonitorMapper monitorMapper) {
        this.landingMapper = landingMapper;
        this.weixinMapper = weixinMapper;
        this.monitorMapper = monitorMapper;
    }

    @GetMapping("/export/landing.csv")
    public ResponseEntity<byte[]> landingCsv() {
        QueryWrapper<Landing> q = new QueryWrapper<Landing>().orderByDesc("id");
        if (!CurrentUser.isAdmin()) q.eq("ownerId", CurrentUser.id());
        List<Landing> rows = landingMapper.selectList(q);

        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"ID", "落地页URL", "备注", "访问量", "转化数", "粉丝数", "状态", "创建时间"});
        for (Landing l : rows) {
            data.add(new String[]{
                    str(l.id), str(l.url), str(l.remarks), str(l.total), str(l.cvscount),
                    str(l.fans), l.enable != null && l.enable == 1 ? "启用" : "停用",
                    l.createTime == null ? "" : l.createTime.toString().replace('T', ' ')
            });
        }
        return csv("landing.csv", data);
    }

    @GetMapping("/export/weixin.csv")
    public ResponseEntity<byte[]> weixinCsv() {
        QueryWrapper<Weixin> q = new QueryWrapper<Weixin>().orderByDesc("id");
        if (!CurrentUser.isAdmin()) q.eq("ownerId", CurrentUser.id());
        List<Weixin> rows = weixinMapper.selectList(q);

        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"ID", "微信号", "昵称", "在线", "访问", "复制", "上报", "权重"});
        for (Weixin w : rows) {
            data.add(new String[]{
                    str(w.id), str(w.wxh), str(w.wxname),
                    w.online != null && w.online == 1 ? "在线" : "离线",
                    str(w.visits), str(w.copyCount), str(w.report), str(w.level)
            });
        }
        return csv("weixin.csv", data);
    }

    @GetMapping("/export/monitor.csv")
    public ResponseEntity<byte[]> monitorCsv() {
        QueryWrapper<Monitor> q = new QueryWrapper<Monitor>().orderByDesc("id").last("LIMIT 50000");
        if (!CurrentUser.isAdmin()) {
            List<Integer> ids = landingMapper.selectList(new QueryWrapper<Landing>()
                            .select("id").eq("ownerId", CurrentUser.id())).stream()
                    .map(l -> l.id).collect(Collectors.toList());
            if (ids.isEmpty()) return csv("monitor.csv", List.<String[]>of(new String[]{"ID", "日期", "时间", "类型", "目标", "微信号", "落地页ID", "停留"}));
            q.in("uid", ids);
        }
        List<Monitor> rows = monitorMapper.selectList(q);

        Map<Integer, String> urls = new LinkedHashMap<>();
        List<Integer> uids = rows.stream().map(m -> m.uid).filter(java.util.Objects::nonNull).distinct().collect(Collectors.toList());
        if (!uids.isEmpty()) {
            for (Landing l : landingMapper.selectBatchIds(uids)) {
                urls.put(l.id, l.url);
            }
        }

        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"ID", "日期", "时间", "类型", "目标", "微信号", "落地页URL", "停留秒"});
        for (Monitor m : rows) {
            String type = (m.type != null && m.type >= 1 && m.type < TYPE_TEXT.length) ? TYPE_TEXT[m.type] : "";
            data.add(new String[]{
                    str(m.id), str(m.cvsdate), str(m.cvstime), type, str(m.target), str(m.wx),
                    urls.getOrDefault(m.uid, ""), str(m.stay)
            });
        }
        return csv("monitor.csv", data);
    }

    private ResponseEntity<byte[]> csv(String filename, List<String[]> rows) {
        StringBuilder sb = new StringBuilder("\uFEFF");
        for (String[] row : rows) {
            for (int i = 0; i < row.length; i++) {
                if (i > 0) sb.append(',');
                sb.append(escape(row[i]));
            }
            sb.append("\r\n");
        }
        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(new MediaType("text", "csv", StandardCharsets.UTF_8))
                .body(bytes);
    }

    private String escape(String v) {
        if (v == null) return "";
        if (v.contains(",") || v.contains("\"") || v.contains("\n")) {
            return "\"" + v.replace("\"", "\"\"") + "\"";
        }
        return v;
    }

    private String str(Object v) {
        return v == null ? "" : String.valueOf(v);
    }
}
