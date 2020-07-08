package com.longyou.gateway.config;

import com.longyou.gateway.filter.CorsWebFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.WebFilter;

@Configuration
public class RouteConfiguration {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public WebFilter croseFilter() {
        return new CorsWebFilter();
    }
}
