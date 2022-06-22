package com.longyou.gateway.security;

import org.cloud.entity.LoginUserDetails;
import org.cloud.vo.LoginUserGetParamsDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public interface UserLoginService {

    public Mono<UserDetails> userLogin(String username, LoginUserGetParamsDTO loginUserGetParamsDTO, String microAppIndex,
        ServerWebExchange swe);

    public void saveLoginLog(String username, LoginUserDetails userDetails, String targetMethodParams, ServerWebExchange swe,
        String successFlag,String message);

    public LoginUserDetails getUserByName(LoginUserGetParamsDTO loginUserGetParamsDTO, ServerWebExchange swe);
}
