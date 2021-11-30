package com.longyou.gateway;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.cloud.utils.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;

import javax.sql.DataSource;

@SpringBootApplication(
        scanBasePackages = {
                "com.longyou.gateway",
                "org.cloud.core.redis",
                "org.cloud.controller",
                "org.cloud.scheduler",
//                "org.cloud.aop",    // 网关没有权限控制，权限控制都在各应用中处理
                "org.cloud.mongo",
                "org.cloud.config.rest"
        }
        ,
        exclude = {
            RabbitAutoConfiguration.class
//                MongoAutoConfiguration.class,
//                GatewayAutoConfiguration.class,
//                MongoDataAutoConfiguration.class,
        }
)
@EnableDiscoveryClient
@EnableRedisWebSession(maxInactiveIntervalInSeconds = 3600, redisNamespace = "system:spring:session")
@EnableFeignClients(basePackages = {"com.longyou.gateway.service.feign", "org.cloud.feign.service"})

@Slf4j
public class GatewayApplication {
    public static void main(String[] args) {
        try{
            SpringApplication.run(GatewayApplication.class, args);
        }catch (Exception e){
            log.error("{}",e);
        }

    }

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
}
