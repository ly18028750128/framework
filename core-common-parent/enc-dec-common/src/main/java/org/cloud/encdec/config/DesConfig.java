package org.cloud.encdec.config;

import lombok.Getter;
import lombok.Setter;
import org.cloud.utils.DESUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "system.des")
@ConditionalOnProperty(prefix = "system.des", name = "enabled", matchIfMissing = true)
@ComponentScan({"org.cloud.encdec"})
public class DesConfig {

    @Setter
    @Getter
    private String iv;

    @Setter
    @Getter
    private String password;

    @Bean
    public DESUtil desUtil() {
        return new DESUtil(iv, password);
    }

}
