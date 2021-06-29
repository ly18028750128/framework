package org.cloud.config;

import lombok.Getter;
import lombok.Setter;
import org.cloud.aop.OperateLogAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "system.OperateLog", name = "enabled", havingValue = "true")
public class OperateLogConfig {

  @Setter
  @Getter
  private String key;
  @Setter
  @Getter
  private String vi;

  @Bean
  public OperateLogAspect operateLogAspect() {
    return new OperateLogAspect();
  }
}
