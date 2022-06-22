package com.longyou.gateway.security;

import com.longyou.gateway.util.ServerWebExchangeContextHolder;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.cloud.constant.CoreConstant;
import org.cloud.entity.LoginUserDetails;
import org.cloud.vo.LoginUserGetParamsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Transactional(propagation = Propagation.NEVER)
@Slf4j
public class SecurityUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    UserLoginService userLoginService;

    /**
     * 通过用户名获取登录用户信息
     *
     * @param username 用户名
     * @return
     */
    @Override
    public Mono<UserDetails> findByUsername(final String username) {
        final LoginUserGetParamsDTO loginUserGetParamsDTO = new LoginUserGetParamsDTO();
        AtomicReference<ServerWebExchange> swe = new AtomicReference<>();
        return ServerWebExchangeContextHolder.get().doOnNext(swe::set).then(Mono.defer(() -> {
            ServerHttpRequest request = swe.get().getRequest();
            final String microAppIndex = request.getHeaders().getFirst(CoreConstant._MICRO_APPINDEX_KEY);
            final String userType = request.getHeaders().getFirst(CoreConstant._USER_TYPE_KEY);
            if (userType != null) {
                loginUserGetParamsDTO.getParamMap().put(CoreConstant._USER_TYPE_KEY, userType);
            } else {
                loginUserGetParamsDTO.getParamMap().remove(CoreConstant._USER_TYPE_KEY);
            }
            // 小程序登录后会获取用户名，并进行校验！
            if (swe.get().getRequest().getURI().toString().endsWith("/auth/login")) {
                return userLoginService.userLogin(username, loginUserGetParamsDTO, microAppIndex, swe.get());
            } else {
                loginUserGetParamsDTO.setUserName(username);
                final LoginUserDetails userDetails = userLoginService.getUserByName(loginUserGetParamsDTO, swe.get());
                if (userDetails == null) {
                    return Mono.empty();
                }
                return Mono.just(userDetails);
            }
        }));
    }
}
