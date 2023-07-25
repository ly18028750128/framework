package org.cloud.config;

import org.cloud.logs.aop.OperateLogAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "system.OperateLog", name = "enabled", matchIfMissing = true)
public class OperateLogConfig {
  @Bean
  public OperateLogAspect operateLogAspect() {
    return new OperateLogAspect();
  }
}
