package com.longyou.gateway.filter;

import com.longyou.gateway.config.vo.CorsConfigVO;
import org.cloud.context.RequestContext;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.CommonUtil;
import org.cloud.utils.RestTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public static ThreadLocal<ServerWebExchange> serverWebExchangeThreadLocal = new ThreadLocal<>();

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

        if (request.getURI().toString().endsWith("/user/info/authentication")) {
            return wfc.filter(swe);
        }

        RequestContext requestContext = new RequestContext();
        final String userinfoUrl = CommonUtil.single().getEnv("system.userinfo.get_url", "http://SPRING-GATEWAY/user/info/authentication");
        HttpHeaders headers = new HttpHeaders();
        if (swe.getRequest().getHeaders().getFirst("cookie") != null) {
            headers.add("cookie", swe.getRequest().getHeaders().getFirst("cookie"));
        }
        if (swe.getRequest().getHeaders().getFirst("authorization") != null) {
            headers.add("authorization", swe.getRequest().getHeaders().getFirst("authorization"));
        }
        RequestContextManager.single().setRequestContext(requestContext);
        if (headers.size() > 0) {
            ResponseEntity<LoginUserDetails> responseEntity = RestTemplateUtil.single().getResponse(userinfoUrl, HttpMethod.GET, swe.getRequest().getHeaders(), LoginUserDetails.class);
            LoginUserDetails user = responseEntity.getBody();
            if (user != null) {
                requestContext.setUser(user);
            }
            RequestContextManager.single().setRequestContext(requestContext);
        }
        return wfc.filter(swe);
    }
}
