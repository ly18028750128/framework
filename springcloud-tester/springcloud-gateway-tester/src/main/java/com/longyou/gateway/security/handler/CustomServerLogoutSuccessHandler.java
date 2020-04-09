package com.longyou.gateway.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.longyou.gateway.security.response.MessageCode;
import com.longyou.gateway.security.response.WsResponse;
import lombok.extern.slf4j.Slf4j;
import org.cloud.constant.CoreConstant;
import org.cloud.core.redis.RedisUtil;
import org.cloud.utils.MD5Encoder;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Transactional(propagation = Propagation.NEVER)
@Slf4j
public class CustomServerLogoutSuccessHandler extends HttpStatusReturningServerLogoutSuccessHandler {

    //    private final Logger logger = LoggerFactory.getLogger(CustomServerLogoutSuccessHandler.class);
    private final HttpStatus httpStatusToReturn = HttpStatus.OK;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        try {
            // 退出时将生成的随机加密值给清空，token直接失效（自动失效时间为24小时），如果想token有效，请不要调用登出接口
            redisUtil.remove(MD5Encoder.encode(webFilterExchange.getExchange().getLogPrefix()));
            final String authStr = webFilterExchange.getExchange().getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            redisUtil.remove(MD5Encoder.encode(authStr));
        } catch (Exception ex) {
            log.error("{}", ex);
        }
        return super.onLogoutSuccess(webFilterExchange, authentication);
    }
}
