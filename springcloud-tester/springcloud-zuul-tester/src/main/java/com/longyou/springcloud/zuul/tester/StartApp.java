package com.longyou.springcloud.zuul.tester;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringCloudApplication
public class StartApp
{
    public static void main(String[] args)
    {
        SpringApplicationBuilder builer = new SpringApplicationBuilder(StartApp.class);
        builer.web(true).run(args);
    }
    

}
