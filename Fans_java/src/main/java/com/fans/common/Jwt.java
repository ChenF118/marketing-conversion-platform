package com.fans.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fans.config.AppProperties;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * HS256 JWT（无额外依赖）。
 * 签发内容与 AdminCt::setToken 完全一致：
 *   iss=key, aud="", iat, nbf, exp(+48h), data.password=管理员ID
 */
@Component
public class Jwt {

    private static final Base64.Encoder B64 = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder B64D = Base64.getUrlDecoder();

    private final AppProperties props;
    private final ObjectMapper om;

    public Jwt(AppProperties props, ObjectMapper om) {
        this.props = props;
        this.om = om;
    }

    public String sign(String adminId) {
        try {
            long now = System.currentTimeMillis() / 1000L;
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("password", adminId);

            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("iss", props.getJwt().getSecret());
            payload.put("aud", "");
            payload.put("iat", now);
            payload.put("nbf", now);
            payload.put("exp", now + props.getJwt().getExpireSeconds());
            payload.put("data", data);

            String header = B64.encodeToString("{\"typ\":\"JWT\",\"alg\":\"HS256\"}".getBytes(StandardCharsets.UTF_8));
            String body = B64.encodeToString(om.writeValueAsBytes(payload));
            String signature = B64.encodeToString(hmac(header + "." + body));
            return header + "." + body + "." + signature;
        } catch (Exception e) {
            throw new IllegalStateException("JWT 签发失败", e);
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> verify(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 3) throw new JwtException("token 格式错误");

            byte[] expected = hmac(parts[0] + "." + parts[1]);
            byte[] actual = B64D.decode(parts[2]);
            if (!MessageDigest.isEqual(expected, actual)) throw new JwtException("token 签名不正确");

            Map<String, Object> payload = om.readValue(B64D.decode(parts[1]), Map.class);
            long now = System.currentTimeMillis() / 1000L;
            long leeway = props.getJwt().getLeewaySeconds();

            Object nbf = payload.get("nbf");
            if (nbf instanceof Number && now + leeway < ((Number) nbf).longValue()) {
                throw new JwtException("token 尚未生效");
            }
            Object exp = payload.get("exp");
            if (exp instanceof Number && now - leeway > ((Number) exp).longValue()) {
                throw new JwtException("token 已过期");
            }
            Object iss = payload.get("iss");
            if (iss != null && !props.getJwt().getSecret().equals(String.valueOf(iss))) {
                throw new JwtException("token 签发者不匹配");
            }
            return payload;
        } catch (JwtException e) {
            throw e;
        } catch (Exception e) {
            throw new JwtException("token 解析失败", e);
        }
    }

    private byte[] hmac(String content) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(props.getJwt().getSecret().getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        return mac.doFinal(content.getBytes(StandardCharsets.UTF_8));
    }

    public static class JwtException extends RuntimeException {
        public JwtException(String message) { super(message); }
        public JwtException(String message, Throwable cause) { super(message, cause); }
    }
}
