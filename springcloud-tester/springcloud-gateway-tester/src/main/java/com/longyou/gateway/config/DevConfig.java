package com.longyou.gateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConditionalOnProperty(name = "spring.application.group")
public class DevConfig {

    @Value("${spring.application.group:}")
    private String group;

    // 此处只是针对开发环境的group的输入URL时不用输入解决方案，有新的服务部署后需要重新启动网关，生产和测试无此问题，
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, DiscoveryClient discoveryClient) {
        final List<String> servers = discoveryClient.getServices();
//        final String group = EnvUtil.single().getEnv("spring.application.group", "");
        RouteLocatorBuilder.Builder routeBuilder = builder.routes();
        if (!"".equalsIgnoreCase(group)) {
            routeBuilder.route(group + "SPRING-GATEWAY_route", r -> r.order(Integer.MIN_VALUE).path("/SPRING-GATEWAY/**")
                    .filters(f -> f.rewritePath("/SPRING-GATEWAY/(?<remaining>.*)", "/${remaining}"))
                    .uri("lb://" + group + "SPRING-GATEWAY"));
            for (String serverNameLowercase : servers) {
                final String serverName = serverNameLowercase.toUpperCase();
                if (serverName.startsWith(group)) {
                    final String serverNameNoGroupName = serverName.substring(group.length());
                    routeBuilder.route(serverName + "_route", r -> r.order(Integer.MIN_VALUE).path("/" + serverNameNoGroupName + "/**")
                            .filters(f -> f.rewritePath("/" + serverNameNoGroupName + "/(?<remaining>.*)", "/${remaining}"))
                            .uri("lb://" + serverName));
                }
            }
        }
        return routeBuilder.build();
    }
}
