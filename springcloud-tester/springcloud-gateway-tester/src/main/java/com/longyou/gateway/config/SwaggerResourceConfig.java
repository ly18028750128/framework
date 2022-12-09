package com.longyou.gateway.config;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.cloud.utils.RestTemplateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

@Slf4j
@Configuration
//@ConditionalOnProperty(name = "spring.application.group")
@ComponentScan(value = {"org.cloud.config.swagger"}, excludeFilters = {
    @Filter(type = FilterType.REGEX, pattern = {"org.cloud.config.swagger.MyBeanPostProcessor",
        "springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider"}),})
public class SwaggerResourceConfig {

    public SwaggerResourceConfig() {
        log.info("开始初始化swagger");
    }

    @Value("${spring.application.group:}")
    private String group;


    @Value("${management.endpoints.web.base-path:/actuator}")
    private String monitorPath;

    @Value("${system.gateway.services:COMMON-SERVICE,SPRING-GATEWAY,CHAIN-BLOCK-SCAN-SERVICE}")  //
    private List<String> servicesList;

    @Bean
    @Primary
    SwaggerResourcesProvider swaggerResourcesProvider(DiscoveryClient discoveryClient) {
        return () -> {
            List<SwaggerResource> resourceList = new ArrayList<>();
            final List<String> servers = discoveryClient.getServices();

            servers.stream().filter(item -> {

                if (item.contains("SPRING-GATEWAY") || item.contains("consul")) {
                    return false;
                }
                if (StringUtils.hasLength(group)) {
                    return item.startsWith(group);
                }
                return servicesList.contains(item);
            }).forEach(item -> {
                try {
                    RestTemplateUtil.single().getResponse("http://" + item + "/" + monitorPath + "/health", HttpMethod.GET, String.class);
                    RestTemplateUtil.single().getResponse("http://" + item + "/v2/api-docs", HttpMethod.GET, String.class);
                    if (StringUtils.hasLength(group)) {
                        resourceList.add(swaggerResource(item, item.replace(group, "")));
                    } else {
                        resourceList.add(swaggerResource(item, item));
                    }

                } catch (Exception e) {
                    log.warn("[{}]服务不健康或者未启用swagger", item);
                }
            });

            return resourceList;
        };
    }


    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation("/" + location + "/v2/api-docs");
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}