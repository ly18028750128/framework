package com.cloud.parammanage.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.cloud.parammanage.common.mapper")
@ComponentScan("com.cloud.parammanage.common.*")
public class AutowireConfiguration {
}
