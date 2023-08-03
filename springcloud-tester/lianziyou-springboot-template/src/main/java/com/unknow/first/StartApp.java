package com.unknow.first;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class})
@ComponentScan({"org.cloud.*"})  // 此处按需添加
@MapperScan({"com.longyou.comm.mapper"})  // 此处按需要添加
@ServletComponentScan({"org.cloud.filter"})
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableHystrix
public class StartApp {

    public static void main(String[] args) {
        try {
            SpringApplication.run(StartApp.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
