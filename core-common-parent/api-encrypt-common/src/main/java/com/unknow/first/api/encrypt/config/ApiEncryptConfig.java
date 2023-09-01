package com.unknow.first.api.encrypt.config;

import com.unknow.first.api.encrypt.aop.ApiEncryptAspect;
import lombok.Getter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;


@Configuration
@ConfigurationProperties(prefix = "system.api.encrypt")
@ConditionalOnProperty(prefix = "system.api", name = "encrypt", matchIfMissing = true)
@ComponentScan("com.unknow.first.api.encrypt")
public class ApiEncryptConfig {

    @Getter
    private static String apiAesKey;  // api加密密钥
    @Getter
    private static String apiAesIv;  // api加密IV

    public void setApiAesKey(String apiAesKey) {
        ApiEncryptConfig.apiAesKey = apiAesKey;
    }

    public void setApiAesIv(String apiAesIv) {
        ApiEncryptConfig.apiAesIv = apiAesIv;
    }

    @Bean
    @Order
    public ApiEncryptAspect apiEncryptAspect() {
        return new ApiEncryptAspect();
    }
}
