package com.longyou.gateway.security;

import com.longyou.gateway.contants.LoginFormField;
import com.longyou.gateway.exception.ValidateCodeAuthFailException;
import com.longyou.gateway.service.feign.IGetUserInfoFeignClient;
import com.longyou.gateway.util.ServerWebExchangeContextHolder;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import lombok.extern.slf4j.Slf4j;
import org.cloud.annotation.AuthLog;
import org.cloud.constant.CoreConstant;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.CollectionUtil;
import org.cloud.utils.MD5Encoder;
import org.cloud.vo.LoginUserGetParamsDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;

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
            final MultiValueMap<String, String> formData = CollectionUtils.toMultiValueMap(new HashMap<>());
            if (swe.get().getRequest().getURI().toString().endsWith("/auth/login")) {
                return userLoginService.userLogin(username, loginUserGetParamsDTO, microAppIndex, formData, swe.get());
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
