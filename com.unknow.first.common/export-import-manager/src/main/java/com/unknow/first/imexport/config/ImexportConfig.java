package com.unknow.first.imexport.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.unknow.first.imexport.feign")
@ComponentScan("com.unknow.first.imexport")
@MapperScan("com.unknow.first.imexport.mapper")
public class ImexportConfig {

}
