package com.longyou.gateway;

import com.alibaba.druid.pool.DruidDataSource;
import org.cloud.utils.SpringContextUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
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
@EnableRedisWebSession
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


}
