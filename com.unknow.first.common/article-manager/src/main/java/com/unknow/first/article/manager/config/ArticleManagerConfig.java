package com.unknow.first.article.manager.config;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.unknow.first.article.manager")
@MapperScan("com.unknow.first.article.manager.mapper")
public class ArticleManagerConfig {

}
