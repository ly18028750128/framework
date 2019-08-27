package org.springcloud.eureka.client.tester;


import org.apache.ibatis.session.SqlSessionFactory;
import org.cloud.utils.SpringContextUtil;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;
import org.springframework.web.client.RestTemplate;


@EnableDiscoveryClient
@ComponentScan({"org.cloud.core.*", "org.springcloud.eureka.client.*"})
@MapperScan("org.springcloud.eureka.client.tester.dao")
@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class})
public class StartApp {
    public static void main(String[] args) {
        SpringApplication.run(StartApp.class, args);
    }

    //    @Bean
//    @LoadBalanced
//    public RestTemplate restTemplate() {
//        return new RestTemplate(factory);
//    }
//
//    @Bean
//    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
//        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
//        factory.setReadTimeout(5000);//单位为ms
//        factory.setConnectTimeout(5000);//单位为ms
//        return factory;
//    }
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }

}
