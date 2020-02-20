package org.cloud.config.rest;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@ConfigurationProperties(prefix = "system.resttemplate")
@Setter
public class ResTemplateFactoryConfig {

    Integer connectionRequestTimeout = 10000;
    Integer connectTimeout = 10000;
    Integer readTimeout = 10000;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(connectionRequestTimeout);
        httpRequestFactory.setConnectTimeout(connectTimeout);
        httpRequestFactory.setReadTimeout(readTimeout);
        return new RestTemplate(httpRequestFactory);
    }

}
