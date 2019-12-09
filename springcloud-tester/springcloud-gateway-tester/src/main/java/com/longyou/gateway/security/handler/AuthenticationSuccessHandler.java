package com.longyou.gateway.security.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.longyou.gateway.security.response.MessageCode;
import com.longyou.gateway.security.response.WsResponse;
import com.longyou.gateway.util.MD5Encoder;
import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.CommonUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Base64;


@Component
public class AuthenticationSuccessHandler extends WebFilterChainServerAuthenticationSuccessHandler {

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest request = exchange.getRequest();
        //设置headers
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        //设置body
        WsResponse wsResponse = WsResponse.success();
        byte[] dataBytes = {};
        ObjectMapper mapper = new ObjectMapper();
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            final String timeSalt=Long.toString(System.currentTimeMillis());

            final StringBuffer authValue = new StringBuffer(userDetails.getUsername() + ":" + MD5Encoder.encode(userDetails.getPassword(), timeSalt));
            authValue.append("::"+timeSalt);
//            webFilterExchange.getExchange().getLogPrefix()
            byte[] authorization = authValue.toString().getBytes();
            String token = Base64.getEncoder().encodeToString(authorization);
            if (userDetails instanceof LoginUserDetails) {
                ((LoginUserDetails) userDetails).setToken(token);
            }
            httpHeaders.add(HttpHeaders.AUTHORIZATION, token);
            wsResponse.setResult(userDetails);
            dataBytes = mapper.writeValueAsBytes(wsResponse);
        } catch (Exception ex) {
            ex.printStackTrace();
            JsonObject result = new JsonObject();
            result.addProperty("status", MessageCode.COMMON_FAILURE.getCode());
            result.addProperty("message", "授权异常");
            dataBytes = result.toString().getBytes();
        }
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(dataBytes);
        return response.writeWith(Mono.just(bodyDataBuffer));
    }


//    private LoginUserDetails buildUser(User user) {
//        LoginUserDetails userDetails = new LoginUserDetails();
//        userDetails.setUsername(user.getUsername());
//        userDetails.setPassword(user.getPassword().substring(user.getPassword().lastIndexOf("}") + 1, user.getPassword().length()));
//        return userDetails;
//    }


}
