package org.cloud.config;

import lombok.Getter;
import lombok.Setter;
import org.cloud.common.service.AESService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "system.aes")
@ConditionalOnProperty(prefix = "system.aes", name = "enabled", havingValue = "true")
public class AESConfig {

  @Setter
  @Getter
  private String key;
  @Setter
  @Getter
  private String vi;

  @Bean
  public AESService aesService() {
    return new AESService(this);
  }

}
