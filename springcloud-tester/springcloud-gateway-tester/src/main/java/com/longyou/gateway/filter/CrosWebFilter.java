package com.longyou.gateway.filter;

import com.longyou.gateway.config.vo.CorsConfigVO;
import org.cloud.context.RequestContext;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.CommonUtil;
import org.cloud.utils.RestTemplateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class CrosWebFilter implements WebFilter {

    Logger logger = LoggerFactory.getLogger(CrosWebFilter.class);

    @Autowired
    private CorsConfigVO corsConfigVO;

    @Autowired
    DiscoveryClient discoveryClient;

    @Value("${spring.application.name}")
    String applicationName;

    public static ThreadLocal<ServerWebExchange> serverWebExchangeThreadLocal = new ThreadLocal<>();

    @Override
    public Mono<Void> filter(ServerWebExchange swe, WebFilterChain wfc) {
        serverWebExchangeThreadLocal.set(swe);
        ServerHttpRequest request = swe.getRequest();
        String uri = request.getURI().getPath();

        if (request.getMethod() == HttpMethod.OPTIONS) {
            this.setCorsHeader(swe);
            swe.getResponse().setStatusCode(HttpStatus.OK);
            return Mono.empty();
        }

        if (uri.startsWith("//")) {
            ServerHttpRequest newRequest = swe.getRequest().mutate().path(uri.replaceFirst("//", "/")).build();
            return wfc.filter(swe.mutate().request(newRequest).build());
        }
        // 如果自己转发给自己那么直接返回
        if (CommonUtil.single().pathMatch(uri, CollectionUtils.arrayToList(new String[]{"/" + applicationName.toUpperCase() + "/**"}))) {
            return wfc.filter(swe);
        }

        // 增加跨域处理
        if (CorsUtils.isCorsRequest(request)) {
            this.setCorsHeader(swe);
        }

        // 需要排除的服务
        List<String> services = discoveryClient.getServices();
        List<String> filterList = new ArrayList<>();
        for (String service : services) {
            filterList.add("/" + service.toUpperCase() + "/**");
        }
        filterList.add("/user/info/authentication");
        filterList.add("/auth/login");
        filterList.add("/auth/logout");
        filterList.add("/monitor/**");
        if (CommonUtil.single().pathMatch(uri, filterList)) {
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
            try {
                ResponseEntity<LoginUserDetails> responseEntity = RestTemplateUtil.single().getResponse(userinfoUrl, HttpMethod.GET, swe.getRequest().getHeaders(), LoginUserDetails.class);
                LoginUserDetails user = responseEntity.getBody();
                if (user != null) {
                    requestContext.setUser(user);
                }
                RequestContextManager.single().setRequestContext(requestContext);
            } catch (Exception e) {
                logger.error("获取用户信息出错，" + e.getMessage());
            }

        }
        return wfc.filter(swe);
    }

    private void setCorsHeader(ServerWebExchange swe) {
        swe.getResponse().getHeaders().add("Access-Control-Allow-Origin", corsConfigVO.getAllowOrgins());
        swe.getResponse().getHeaders().add("Access-Control-Allow-Methods", corsConfigVO.getAllowMethods());
        swe.getResponse().getHeaders().add("Access-Control-Max-Age", "3600");
        swe.getResponse().getHeaders().add("Access-Control-Allow-Headers", corsConfigVO.getAllowHeaders());

    }

}
