package com.longyou.comm.starter;

import com.github.pagehelper.PageHelper;
import org.cloud.utils.SpringContextUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;
@EnableDiscoveryClient
@ComponentScan({"org.cloud.*","com.longyou.comm.*"})
@MapperScan("com.longyou.comm.mapper")
@ServletComponentScan({"org.cloud.filter"})
@SpringBootApplication(exclude={ HibernateJpaAutoConfiguration.class})
public class CommonServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CommonServiceApplication.class,args);
    }

    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        /**
         *该参数默认为false
         *设置为true时，会将RowBounds第一个参数offset当成pageNum页码使用
         *和startPage中的pageNum效果一样
         */
        p.setProperty("offsetAsPageNum", "true");
        /**
         *该参数默认为false
         *设置为true时，使用RowBounds分页会进行count查询
         */
        p.setProperty("rowBoundsWithCount", "true");
        /**
         *3.3.0版本可用 - 分页参数合理化，默认false禁用
         *启用合理化时，如果pageNum<1会查询第一页，如果pageNum>pages会查询最后一页
         *禁用合理化时，如果pageNum<1或pageNum>pages会返回空数据
         */
        p.setProperty("reasonable", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }

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
