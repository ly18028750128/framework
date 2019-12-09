package com.longyou.gateway.config;

import com.longyou.gateway.config.vo.CorsConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;

@Configuration
public class RouteConfiguration {

    @Autowired
    private CorsConfigVO corsConfigVO;

//    @Bean
//    public CorsWebFilter corsFilter() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.addAllowedMethod(corsConfigVO.getAllowMethods());
//        config.addAllowedOrigin(corsConfigVO.getAllowOrgins());
//        config.addAllowedHeader(corsConfigVO.getAllowHeaders());
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
//        source.registerCorsConfiguration("/**", config);
//
//        return new CorsWebFilter(source);
//    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public WebFilter croseFilter() {
        return new WebFilter() {
            @Override
            public Mono<Void> filter(ServerWebExchange swe, WebFilterChain wfc) {
                ServerHttpRequest request = swe.getRequest();
                if (CorsUtils.isCorsRequest(request)) {
                    ServerHttpResponse response = swe.getResponse();
                    HttpHeaders headers = response.getHeaders();
                    headers.add("Access-Control-Allow-Origin", corsConfigVO.getAllowOrgins());
                    headers.add("Access-Control-Allow-Methods", corsConfigVO.getAllowMethods());
                    headers.add("Access-Control-Max-Age", "3600");
                    headers.add("Access-Control-Allow-Headers", corsConfigVO.getAllowHeaders());
                    if (request.getMethod() == HttpMethod.OPTIONS) {
                        response.setStatusCode(HttpStatus.OK);
                        return Mono.empty();
                    }
                }
                return wfc.filter(swe);
            }
        };
    }

    /**
     * 如果使用了注册中心（如：Eureka），进行控制则需要增加如下配置
     */
    @Bean
    public RouteDefinitionLocator discoveryClientRouteDefinitionLocator(DiscoveryClient discoveryClient) {
        return new DiscoveryClientRouteDefinitionLocator(discoveryClient, null);
    }
}
