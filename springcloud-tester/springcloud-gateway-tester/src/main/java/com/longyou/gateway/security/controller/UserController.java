package com.longyou.gateway.security.controller;

import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.HttpServletUtil;
import org.cloud.utils.http.OKHttpClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/user/info", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Transactional(propagation = Propagation.NEVER)
public class UserController {
    @GetMapping("/authentication")
    public Mono<UserDetails> getAuthentication(Authentication authentication,ServletServerHttpResponse response) {
        if (authentication == null) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return Mono.empty();
        }
        UserDetails user = (UserDetails) authentication.getPrincipal();
        if (user != null) {
            return Mono.just(user);
        }
        return Mono.empty();
    }
}
