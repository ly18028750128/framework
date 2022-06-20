package com.longyou.gateway.filter;

import com.longyou.gateway.config.vo.CorsConfigVO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.cloud.context.RequestContext;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.CommonUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
public class CorsWebFilter implements WebFilter {

    @Autowired
    DiscoveryClient discoveryClient;

    @Value("${spring.application.name}")
    String applicationName;


    @NotNull
    @Override
    public Mono<Void> filter(@NotNull ServerWebExchange swe, @NotNull WebFilterChain wfc) {
//        ServerWebExchangeThreadLocal.value.set(swe);
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
        if (CommonUtil.single().pathMatch(uri, Collections.singletonList("/" + applicationName.toUpperCase() + "/**"))) {
            return wfc.filter(swe);
        }

        // 增加跨域处理
        this.setCorsHeader(swe);
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
        filterList.add("/resource/register/all");
        filterList.add("/quartz/job/all");

        if (CommonUtil.single().pathMatch(uri, filterList)) {
            return wfc.filter(swe);
        }
        final RequestContext requestContext = new RequestContext();
        final AtomicReference<LoginUserDetails> user = new AtomicReference<>();
        return swe.getSession().doOnNext(webSession -> {
            try {
                SecurityContextImpl securityContext = webSession.getAttribute("SPRING_SECURITY_CONTEXT");
                if (securityContext != null) {
                    user.set((LoginUserDetails) securityContext.getAuthentication().getPrincipal());
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }).then(Mono.defer(() -> {
            requestContext.setUser(user.get());
            RequestContextManager.single().setRequestContext(requestContext);
//            ServerWebExchangeThreadLocal.value.set(swe);
            return wfc.filter(swe);
        }));
    }

    @Autowired
    private CorsConfigVO corsConfigVO;

    private void setCorsHeader(ServerWebExchange swe) {
        if (CorsUtils.isCorsRequest(swe.getRequest())) {
            swe.getResponse().getHeaders().add("Access-Control-Allow-Origin", swe.getRequest().getHeaders().getOrigin());
            swe.getResponse().getHeaders().add("Access-Control-Allow-Methods", corsConfigVO.getAllowMethods());
            swe.getResponse().getHeaders().add("Access-Control-Max-Age", "3600");
            swe.getResponse().getHeaders().add("Access-Control-Allow-Headers", corsConfigVO.getAllowHeaders());
            swe.getResponse().getHeaders().add("Access-Control-Allow-Credentials", "true");
        }
    }
}
