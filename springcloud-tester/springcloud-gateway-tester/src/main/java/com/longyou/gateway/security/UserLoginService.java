package com.longyou.gateway.security;

import org.cloud.entity.LoginUserDetails;
import org.cloud.vo.LoginUserGetParamsDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface UserLoginService {

    public Mono<UserDetails> userLogin(String username, LoginUserGetParamsDTO loginUserGetParamsDTO, String microAppIndex,
        MultiValueMap<String, String> formData, ServerWebExchange swe);

    public LoginUserDetails getUserByName(LoginUserGetParamsDTO loginUserGetParamsDTO, ServerWebExchange swe);
}
