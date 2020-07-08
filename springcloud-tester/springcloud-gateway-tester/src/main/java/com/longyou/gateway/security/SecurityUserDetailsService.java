package com.longyou.gateway.security;

import com.longyou.gateway.contants.LoginFormField;
import com.longyou.gateway.filter.CorsWebFilter;
import com.longyou.gateway.service.feign.IGetUserInfoFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.cloud.constant.CoreConstant;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.exception.BusinessException;
import org.cloud.utils.CollectionUtil;
import org.cloud.utils.MD5Encoder;
import org.cloud.vo.LoginUserGetParamsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;

@Component
@Transactional(propagation = Propagation.NEVER)
@Slf4j
public class SecurityUserDetailsService implements ReactiveUserDetailsService {

    @Autowired
    RedisUtil redisUtil;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        final LoginUserGetParamsDTO loginUserGetParamsDTO = new LoginUserGetParamsDTO();
        final ServerWebExchange swe = CorsWebFilter.serverWebExchangeThreadLocal.get();
        ServerHttpRequest request = swe.getRequest();
        final String microAppindex = request.getHeaders().getFirst(CoreConstant._MICRO_APPINDEX_KEY);
        final String userType = request.getHeaders().getFirst(CoreConstant._USER_TYPE_KEY);

        if (userType != null) {
            loginUserGetParamsDTO.getParams().put(CoreConstant._USER_TYPE_KEY, userType);
        } else {
            loginUserGetParamsDTO.getParams().remove(CoreConstant._USER_TYPE_KEY);
        }
        // 小程序登录后会获取用户名，并进行校验！

        final MultiValueMap<String, String> formData = CollectionUtils.toMultiValueMap(new HashMap<>());
        return swe.getFormData().doOnNext(
                multiValueMap -> {
                    formData.putAll(multiValueMap);
                })
                .then(Mono.defer(
                        () -> {
                            final String validateRedisKey = formData.getFirst(LoginFormField.VALIDATE_REDIS_KEY.field());
                            if (CollectionUtil.single().isNotEmpty(validateRedisKey)) {
                                final String validateCode = redisUtil.get(validateRedisKey);
                                final String userValidateCode = formData.getFirst(LoginFormField.VALIDATE_CODE.field());
                                if (CollectionUtil.single().isEmpty(validateCode)) {
                                    return Mono.error(new BusinessException("验证码错误"));
                                } else if (!validateCode.equalsIgnoreCase(userValidateCode)) {
                                    return Mono.error(new BusinessException("验证码错误"));
                                }
                            }

                            if (microAppindex != null) {
                                final String weixinLoginCode = formData.getFirst(LoginFormField.PASSWORD.field());
                                loginUserGetParamsDTO.setUserName(weixinLoginCode);
                                loginUserGetParamsDTO.getParams().put(CoreConstant._MICRO_LOGIN_CODE_KEY, weixinLoginCode);
                                loginUserGetParamsDTO.setMicroAppIndex(Integer.parseInt(microAppindex));
                                final LoginUserDetails userDetails = getUserByName(loginUserGetParamsDTO);
                                User.withUserDetails(userDetails).build();
                                return Mono.just(userDetails);
                            } else {
                                loginUserGetParamsDTO.setUserName(username);
                                final LoginUserDetails userDetails = getUserByName(loginUserGetParamsDTO);
                                if (userDetails != null && StringUtils.equals(userDetails.getUsername(), username)) {
                                    User.withUserDetails(userDetails).build();
                                    return Mono.just(userDetails);
                                } else {
                                    return Mono.error(new UsernameNotFoundException("User Not Found"));
                                }
                            }
                        }
                ));
    }

    @Autowired
    IGetUserInfoFeignClient getUserInfoFeignClient;

    private LoginUserDetails getUserByName(LoginUserGetParamsDTO loginUserGetParamsDTO) {
        LoginUserDetails loginUserDetails = null;
        if (CorsWebFilter.serverWebExchangeThreadLocal.get() == null
                || CorsWebFilter.serverWebExchangeThreadLocal.get().getRequest() == null
                || CorsWebFilter.serverWebExchangeThreadLocal.get().getRequest().getHeaders() == null) {
            return null;
        }
        final String token = CorsWebFilter.serverWebExchangeThreadLocal.get().getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        // 如果带了token那么从缓存中获取数据
        if (token != null && !"0".equals(token) && token.length() > 15) {
            return redisUtil.get(CoreConstant._BASIC64_TOKEN_USER_CACHE_KEY + MD5Encoder.encode(token));
        } else if (loginUserDetails == null) {
            return getUserInfoFeignClient.getUserInfo(loginUserGetParamsDTO);
        }
        return null;
    }
}
