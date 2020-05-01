package com.longyou.gateway;

import com.alibaba.druid.pool.DruidDataSource;
import org.cloud.utils.CommonUtil;
import org.cloud.utils.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;

import javax.sql.DataSource;
import java.util.List;

@SpringBootApplication(
        scanBasePackages = {
                "com.longyou.gateway",
                "org.cloud.core.redis",
                "org.cloud.controller",
                "org.cloud.scheduler",
                "org.cloud.aop",
                "org.cloud.mongo",
                "org.cloud.config.rest"
        }
        //        ,
//        exclude = {
//                MongoAutoConfiguration.class,
//                MongoDataAutoConfiguration.class}
)
@EnableDiscoveryClient
@EnableRedisWebSession(maxInactiveIntervalInSeconds = 3600, redisNamespace = "system:spring:session")
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

//    @Bean
//    @LoadBalanced
//    public RestTemplate restTemplate() {
//        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
//        httpRequestFactory.setConnectionRequestTimeout(3000);
//        httpRequestFactory.setConnectTimeout(3000);
//        httpRequestFactory.setReadTimeout(3000);
//        return new RestTemplate(httpRequestFactory);
//    }


    @Bean
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DruidDataSource druidDataSource() {
        return new DruidDataSource();
    }


    /**
     * 配置Quartz独立数据源的配置
     */
    @Bean
    @QuartzDataSource
    @ConfigurationProperties(prefix = "spring.datasource.quartz")
    public DataSource quartzDataSource() {
        return new DruidDataSource();
    }

    /**
     * 如果使用了注册中心（如：Eureka），进行控制则需要增加如下配置
     */
    @Bean
    @ConditionalOnBean(ReactiveDiscoveryClient.class)
    @ConditionalOnProperty(name = "spring.cloud.gateway.discovery.locator.enabled")
    public RouteDefinitionLocator discoveryClientRouteDefinitionLocator(ReactiveDiscoveryClient discoveryClient, DiscoveryLocatorProperties properties) {
        return new DiscoveryClientRouteDefinitionLocator(discoveryClient, properties);
    }


    // 此处只是针对开发环境的group的输入URL时不用输入解决方案，有新的服务部署后需要重新启动网关，生产和测试无此问题，
    @Bean
    @ConditionalOnProperty(name = "spring.application.group")
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, DiscoveryClient discoveryClient) {

        final List<String> servers = discoveryClient.getServices();
        final String group = CommonUtil.single().getEnv("spring.application.group", "");
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
