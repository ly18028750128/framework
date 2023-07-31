package com.longyou.gateway;

import com.alibaba.druid.pool.DruidDataSource;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.cloud.utils.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;

@SpringBootApplication(exclude = {RabbitAutoConfiguration.class})
@EnableDiscoveryClient
@EnableRedisWebSession(maxInactiveIntervalInSeconds = 3600, redisNamespace = "system:spring:session")
@EnableFeignClients(basePackages = {"com.longyou.gateway.service.feign"})
@Slf4j
public class GatewayApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(GatewayApplication.class, args);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Bean
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource() {
        return new DruidDataSource();
    }

    /**
     * 配置Quartz独立数据源的配置
     */
//    @Bean
//    @QuartzDataSource
//    @ConfigurationProperties(prefix = "spring.datasource.quartz")
//    public DataSource quartzDataSource() {
//        return new DruidDataSource();
//    }

    /**
     * 如果使用了注册中心（如：Eureka），进行控制则需要增加如下配置
     */
//    @Bean
//    @ConditionalOnBean(ReactiveDiscoveryClient.class)
//    @ConditionalOnProperty(name = "spring.cloud.gateway.discovery.locator.enabled")
//    public RouteDefinitionLocator discoveryClientRouteDefinitionLocator(ReactiveDiscoveryClient discoveryClient, DiscoveryLocatorProperties properties) {
//        return new DiscoveryClientRouteDefinitionLocator(discoveryClient, properties);
//    }
}
