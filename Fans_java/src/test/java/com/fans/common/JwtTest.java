package com.fans.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fans.config.AppProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtTest {

    private Jwt jwt;

    @BeforeEach
    void setUp() {
        AppProperties props = new AppProperties();
        props.getJwt().setSecret("unit-test-secret");
        jwt = new Jwt(props, new ObjectMapper());
    }

    @Test
    void signAndVerifyRoundTrip() {
        String token = jwt.sign("7");
        Map<String, Object> claims = jwt.verify(token);

        assertEquals("unit-test-secret", claims.get("iss"));
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) claims.get("data");
        assertEquals("7", data.get("password"));
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void tamperedTokenIsRejected() {
        String token = jwt.sign("7");
        String tampered = token.substring(0, token.length() - 3) + "abc";
        assertThrows(Jwt.JwtException.class, () -> jwt.verify(tampered));
    }

    @Test
    void invalidFormatIsRejected() {
        assertThrows(Jwt.JwtException.class, () -> jwt.verify("not-a-jwt"));
    }
}
