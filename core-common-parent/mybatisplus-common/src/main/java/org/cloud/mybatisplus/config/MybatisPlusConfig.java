package org.cloud.mybatisplus.config;


import org.cloud.mybatisplus.mybatis.interceptor.UserInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("org.cloud.mybatisplus.mybatis.dynamic")
@ComponentScan("org.cloud.mybatisplus")
public class MybatisPlusConfig {

    @Bean
    public UserInterceptor userInterceptor() {
        return new UserInterceptor();
    }


}
