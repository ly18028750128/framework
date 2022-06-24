package com.longyou.gateway.security.handler;


import static com.longyou.gateway.security.response.MessageCode.COMMON_AUTHORIZED_FAILURE;

import com.google.gson.JsonObject;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.authentication.HttpBasicServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Transactional(propagation = Propagation.NEVER)
public class CustomHttpBasicServerAuthenticationEntryPoint extends HttpBasicServerAuthenticationEntryPoint {

    private String headerValue = createHeaderValue("Realm");

    public CustomHttpBasicServerAuthenticationEntryPoint() {
    }

    public void setRealm(String realm) {
        this.headerValue = createHeaderValue(realm);
    }

    private static String createHeaderValue(String realm) {
        Assert.notNull(realm, "realm cannot be null");
        String WWW_AUTHENTICATE_FORMAT = "Basic realm=\"%s\"";
        return String.format(WWW_AUTHENTICATE_FORMAT, realm);
    }

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException e) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json; charset=UTF-8");
        response.getHeaders().set(HttpHeaders.AUTHORIZATION, this.headerValue);
        JsonObject result = new JsonObject();
        result.addProperty("status", COMMON_AUTHORIZED_FAILURE.code);
        result.addProperty("message", COMMON_AUTHORIZED_FAILURE.message);
        byte[] dataBytes = result.toString().getBytes();
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(dataBytes);
        return response.writeWith(Mono.just(bodyDataBuffer));
    }
}
