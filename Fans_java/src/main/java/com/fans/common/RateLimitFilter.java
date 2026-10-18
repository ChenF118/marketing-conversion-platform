package com.fans.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fans.config.AppProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 轻量级滑动窗口限流：按 IP 维度，保护登录与公开埋点接口，防止被刷。
 * 单机内存实现，集群部署时可替换为 Redis 令牌桶。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class RateLimitFilter extends OncePerRequestFilter {

    private final AppProperties props;
    private final ObjectMapper om;
    private final Map<String, Deque<Long>> buckets = new ConcurrentHashMap<>();

    public RateLimitFilter(AppProperties props, ObjectMapper om) {
        this.props = props;
        this.om = om;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        if (!props.getRateLimit().isEnabled()) return true;
        String path = request.getRequestURI();
        return path.startsWith("/uploads/") || path.startsWith("/demo/") || path.startsWith("/sdk/")
                || path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String ip = clientIp(request);
        boolean login = request.getRequestURI().startsWith("/admin/login");
        int limit = login ? props.getRateLimit().getLoginPerMinute() : props.getRateLimit().getPerMinute();
        String key = ip + (login ? ":login" : ":api");

        if (!allow(key, limit)) {
            response.setStatus(429);
            response.setContentType("application/json;charset=UTF-8");
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("code", 429);
            body.put("message", "请求过于频繁，请稍后再试");
            response.getWriter().write(om.writeValueAsString(body));
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean allow(String key, int limit) {
        long now = System.currentTimeMillis();
        Deque<Long> window = buckets.computeIfAbsent(key, k -> new ArrayDeque<>());
        synchronized (window) {
            while (!window.isEmpty() && now - window.peekFirst() > 60_000L) {
                window.pollFirst();
            }
            if (window.size() >= limit) return false;
            window.addLast(now);
            return true;
        }
    }

    private String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isEmpty()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
