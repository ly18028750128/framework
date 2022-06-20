package com.longyou.gateway.util;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class ServerWebExchangeContextHolder {

    public static final Class<ServerWebExchange> CONTEXT_KEY = ServerWebExchange.class;

    public static Mono<ServerWebExchange> get() {
        return Mono.subscriberContext().map(ctx -> ctx.get(CONTEXT_KEY));
    }
}