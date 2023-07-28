package com.unkow.first.photo.manager.config;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.unkow.first.photo.manager")
@MapperScan("com.unkow.first.photo.manager.mapper")
public class PhotoManagerConfig {

}
