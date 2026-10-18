package com.fans.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/** 应用配置（支持环境变量覆盖）。 */
@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    private Security security = new Security();
    private Jwt jwt = new Jwt();
    private Wechat wechat = new Wechat();
    private RateLimit rateLimit = new RateLimit();
    private Upload upload = new Upload();

    public Security getSecurity() { return security; }
    public void setSecurity(Security security) { this.security = security; }
    public Jwt getJwt() { return jwt; }
    public void setJwt(Jwt jwt) { this.jwt = jwt; }
    public Wechat getWechat() { return wechat; }
    public void setWechat(Wechat wechat) { this.wechat = wechat; }
    public RateLimit getRateLimit() { return rateLimit; }
    public void setRateLimit(RateLimit rateLimit) { this.rateLimit = rateLimit; }
    public Upload getUpload() { return upload; }
    public void setUpload(Upload upload) { this.upload = upload; }

    public static class Security {
        private boolean enabled = true;
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
    }

    public static class Jwt {
        private String secret = "change-me-in-production";
        private long expireSeconds = 48 * 60 * 60L;
        private long leewaySeconds = 60L;
        public String getSecret() { return secret; }
        public void setSecret(String secret) { this.secret = secret; }
        public long getExpireSeconds() { return expireSeconds; }
        public void setExpireSeconds(long expireSeconds) { this.expireSeconds = expireSeconds; }
        public long getLeewaySeconds() { return leewaySeconds; }
        public void setLeewaySeconds(long leewaySeconds) { this.leewaySeconds = leewaySeconds; }
    }

    public static class Wechat {
        private String appid = "";
        private String secret = "";
        private String schemeAppid = "";
        private String schemeSecret = "";
        public String getAppid() { return appid; }
        public void setAppid(String appid) { this.appid = appid; }
        public String getSecret() { return secret; }
        public void setSecret(String secret) { this.secret = secret; }
        public String getSchemeAppid() { return schemeAppid; }
        public void setSchemeAppid(String schemeAppid) { this.schemeAppid = schemeAppid; }
        public String getSchemeSecret() { return schemeSecret; }
        public void setSchemeSecret(String schemeSecret) { this.schemeSecret = schemeSecret; }
    }

    public static class RateLimit {
        private boolean enabled = true;
        private int perMinute = 120;
        private int loginPerMinute = 10;
        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }
        public int getPerMinute() { return perMinute; }
        public void setPerMinute(int perMinute) { this.perMinute = perMinute; }
        public int getLoginPerMinute() { return loginPerMinute; }
        public void setLoginPerMinute(int loginPerMinute) { this.loginPerMinute = loginPerMinute; }
    }

    public static class Upload {
        /** 上传目录（相对启动目录） */
        private String dir = "uploads";
        private long maxSizeMb = 5;
        public String getDir() { return dir; }
        public void setDir(String dir) { this.dir = dir; }
        public long getMaxSizeMb() { return maxSizeMb; }
        public void setMaxSizeMb(long maxSizeMb) { this.maxSizeMb = maxSizeMb; }
    }
}
