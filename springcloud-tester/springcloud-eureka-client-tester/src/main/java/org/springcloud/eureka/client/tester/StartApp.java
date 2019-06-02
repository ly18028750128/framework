package org.springcloud.eureka.client.tester;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoConfiguration
@ComponentScan({"org.cloud.core.*","org.springcloud.eureka.client.*"})
public class StartApp 
{
    public static void main( String[] args ){
        SpringApplicationBuilder builer = new SpringApplicationBuilder(StartApp.class);
        builer.web(true).run(args);
    }
}
