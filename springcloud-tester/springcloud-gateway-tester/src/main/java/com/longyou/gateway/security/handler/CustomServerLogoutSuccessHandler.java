package com.longyou.gateway.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.longyou.gateway.security.response.MessageCode;
import com.longyou.gateway.security.response.WsResponse;
import org.cloud.constant.CoreConstant;
import org.cloud.core.redis.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.HttpStatusReturningServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class CustomServerLogoutSuccessHandler extends HttpStatusReturningServerLogoutSuccessHandler {

    private final Logger logger = LoggerFactory.getLogger(CustomServerLogoutSuccessHandler.class);
    private final HttpStatus httpStatusToReturn=HttpStatus.OK;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpResponse response = exchange.getResponse();
        //设置headers
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        //设置body
        WsResponse<String> wsResponse = WsResponse.success();
        byte[] dataBytes = {};
        try {
            final String userBasic64RandomKey = authentication.getName()+webFilterExchange.getExchange().getLogPrefix();
            redisUtil.remove(CoreConstant._REDIS_USER_SUCCESS_TOKEN_PREFIX+userBasic64RandomKey);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
        return super.onLogoutSuccess(webFilterExchange,authentication);
    }
}
