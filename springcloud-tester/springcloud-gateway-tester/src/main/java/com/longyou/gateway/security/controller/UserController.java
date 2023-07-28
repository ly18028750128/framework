package com.longyou.gateway.security.controller;

import org.cloud.dimension.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.entity.LoginUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/user/info", produces = MediaType.APPLICATION_JSON_VALUE)
@Transactional(propagation = Propagation.NEVER)
public class UserController {
    @GetMapping("/authentication")
    @SystemResource(value = "/demo谷歌验证码测试", authMethod = CoreConstant.AuthMethod.ALLSYSTEMUSER)
    public Mono<UserDetails> getAuthentication(Authentication authentication, ServerHttpResponse response) {
        if (authentication == null) {
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return Mono.empty();
        }
        LoginUserDetails user = (LoginUserDetails) authentication.getPrincipal();
        if (user != null) {
            user.setPassword("***********");
            return Mono.just(user);
        }
        return Mono.empty();
    }
}
