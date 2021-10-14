package org.cloud.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@ConditionalOnProperty(prefix = "system.canal", name = "enabled", havingValue = "true")
public class CanalConfig {

}
