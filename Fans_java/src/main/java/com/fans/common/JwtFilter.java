package com.fans.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fans.config.AppProperties;
import com.fans.entity.Admin;
import com.fans.mapper.AdminMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 鉴权 + 当前用户上下文。
 * 公开白名单：登录/注册、埋点上报、腾讯回传、落地页配置查询、演示页、SDK、上传文件、接口文档。
 * 其余接口需要 Authorization: <token>（兼容 Bearer）。
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final List<String> PUBLIC_PATHS = List.of(
            "/admin/login",
            "/admin/rgs",
            "/monitor/append",
            "/monitor/inctotal",
            "/tencent/",
            "/access/",
            "/cvslink/flg",
            "/demo/",
            "/sdk/",
            "/uploads/",
            "/swagger-ui",
            "/v3/api-docs",
            "/wx/scheme",
            "/error"
    );

    private final AppProperties props;
    private final Jwt jwt;
    private final ObjectMapper om;
    private final AdminMapper adminMapper;

    public JwtFilter(AppProperties props, Jwt jwt, ObjectMapper om, AdminMapper adminMapper) {
        this.props = props;
        this.jwt = jwt;
        this.om = om;
        this.adminMapper = adminMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (!props.getSecurity().isEnabled()) return true;
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) return true;

        String path = request.getRequestURI();
        for (String p : PUBLIC_PATHS) {
            if (path.equals(p) || path.startsWith(p)) return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String token = request.getHeader("authorization");
        if (token == null) token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) token = token.substring(7);

        if (token == null || token.isEmpty() || "undefined".equals(token)) {
            reject(response);
            return;
        }

        try {
            Map<String, Object> claims = jwt.verify(token);
            Object dataObj = claims.get("data");
            Integer adminId = null;
            if (dataObj instanceof Map<?, ?> data) {
                adminId = Req.toInteger(data.get("password"));
            }
            if (adminId == null) {
                reject(response);
                return;
            }
            Admin admin = adminMapper.selectById(adminId);
            if (admin == null) {
                reject(response);
                return;
            }
            CurrentUser.set(new CurrentUser.Info(admin.id, admin.power == null ? 0 : admin.power));
            try {
                chain.doFilter(request, response);
            } finally {
                CurrentUser.clear();
            }
        } catch (RuntimeException e) {
            CurrentUser.clear();
            reject(response);
        }
    }

    private void reject(HttpServletResponse response) throws IOException {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", "登陆状态失效，请重新登录");
        body.put("code", 401);
        response.getWriter().write(om.writeValueAsString(body));
    }
}
