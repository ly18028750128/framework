package org.cloud.config;


import org.cloud.mybatis.interceptor.UserInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("org.cloud.mybatis.dynamic")
public class MybatisPlusConfig {

    @Bean
    public UserInterceptor userInterceptor() {
        return new UserInterceptor();
    }
}
