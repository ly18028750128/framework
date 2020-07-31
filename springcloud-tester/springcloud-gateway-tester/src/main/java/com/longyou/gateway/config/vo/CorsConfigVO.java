package com.longyou.gateway.config.vo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "system.route.cors")
public class CorsConfigVO {
    private String allowHeaders;
    private String allowMethods;
    private String allowOrgins;
    private String allowEpose;

    public String getAllowHeaders() {
        return allowHeaders;
    }

    public void setAllowHeaders(String allowHeaders) {
        this.allowHeaders = allowHeaders;
    }

    public String getAllowMethods() {
        return allowMethods;
    }

    public void setAllowMethods(String allowMethods) {
        this.allowMethods = allowMethods;
    }

    public String getAllowOrgins() {
        return allowOrgins;
    }

    public void setAllowOrgins(String allowOrgins) {
        this.allowOrgins = allowOrgins;
    }

    public String getAllowEpose() {
        return allowEpose;
    }

    public void setAllowEpose(String allowEpose) {
        this.allowEpose = allowEpose;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    private String maxAge;
}

