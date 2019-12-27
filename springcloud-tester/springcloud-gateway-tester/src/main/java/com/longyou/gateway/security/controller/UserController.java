package com.longyou.gateway.security.controller;

import org.cloud.utils.HttpServletUtil;
import org.cloud.utils.http.OKHttpClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user/info")
public class UserController {
    @RequestMapping("/authentication")
    public Mono<UserDetails>  getAuthentication(Authentication authentication, ServerHttpResponse response) {
        if(authentication==null || authentication.getPrincipal() == null){
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return Mono.empty();
        }else{
            UserDetails user = (UserDetails) authentication.getPrincipal();
            return Mono.just(user);
        }
    }


}
