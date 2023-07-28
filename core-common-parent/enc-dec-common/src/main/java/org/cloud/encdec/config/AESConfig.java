package org.cloud.encdec.config;

import lombok.Getter;
import lombok.Setter;
import org.cloud.encdec.service.AESService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "system.aes")
@ConditionalOnProperty(prefix = "system.aes", name = "enabled", matchIfMissing = true)
@ComponentScan({"org.cloud.encdec"})
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
