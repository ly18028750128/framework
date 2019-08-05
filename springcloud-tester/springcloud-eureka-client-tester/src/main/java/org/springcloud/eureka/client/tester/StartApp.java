package org.springcloud.eureka.client.tester;


import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


@EnableDiscoveryClient
@ComponentScan({"org.cloud.core.*","org.springcloud.eureka.client.*"})
@MapperScan("org.springcloud.eureka.client.tester.dao")
@SpringBootApplication(exclude={ HibernateJpaAutoConfiguration.class})
public class StartApp 
{
    public static void main( String[] args ){
        SpringApplication.run(StartApp.class,args);
    }


}
