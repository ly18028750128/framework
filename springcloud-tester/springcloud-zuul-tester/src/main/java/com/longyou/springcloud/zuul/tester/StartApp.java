package com.longyou.springcloud.zuul.tester;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableZuulProxy
@EnableDiscoveryClient
@SpringCloudApplication
@EnableAutoConfiguration
@EnableTransactionManagement
public class StartApp
{
    public static void main(String[] args)
    {
        SpringApplicationBuilder builer = new SpringApplicationBuilder(StartApp.class);
        builer.logStartupInfo(true).run(args);
    }
}
