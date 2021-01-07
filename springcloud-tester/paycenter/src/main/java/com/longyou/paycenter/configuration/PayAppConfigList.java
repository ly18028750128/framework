package com.longyou.paycenter.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "system.pay-platforms")
@Data
public class PayAppConfigList {
    public String description="支付平台配置";
    private List<PayAppConfig> platformList = new ArrayList<>();
}
