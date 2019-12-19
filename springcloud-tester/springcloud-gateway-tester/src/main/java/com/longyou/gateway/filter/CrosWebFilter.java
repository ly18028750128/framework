package com.longyou.gateway.filter;

import com.longyou.gateway.config.vo.CorsConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class CrosWebFilter implements WebFilter {

    @Autowired
    private CorsConfigVO corsConfigVO;

    public static ThreadLocal<ServerWebExchange> serverWebExchangeThreadLocal=new ThreadLocal<>();
    @Override
    public Mono<Void> filter(ServerWebExchange swe, WebFilterChain wfc) {

        serverWebExchangeThreadLocal.set(swe);

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
        String uri = swe.getRequest().getURI().getPath();
        if (uri.endsWith("/")) {
            ServerHttpRequest newRequest = swe.getRequest().mutate().path(uri + "index.html").build();
            return wfc.filter(swe.mutate().request(newRequest).build());
        }
        if (uri.startsWith("//")) {
            ServerHttpRequest newRequest = swe.getRequest().mutate().path(uri.replaceFirst("//", "/")).build();
            return wfc.filter(swe.mutate().request(newRequest).build());
        }
        return wfc.filter(swe);
    }
}
