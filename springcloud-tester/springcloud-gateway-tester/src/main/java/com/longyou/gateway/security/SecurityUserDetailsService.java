package com.longyou.gateway.security;

import com.longyou.gateway.contants.LoginFormField;
import com.longyou.gateway.exception.ValidateCodeAuthFailException;
import com.longyou.gateway.filter.CorsWebFilter;
import com.longyou.gateway.service.feign.IGetUserInfoFeignClient;
import com.rabbitmq.client.AMQP.Basic.Get;
import lombok.extern.slf4j.Slf4j;
import org.cloud.constant.CoreConstant;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.CollectionUtil;
import org.cloud.utils.MD5Encoder;
import org.cloud.vo.LoginUserGetParamsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    RedisUtil redisUtil;

    @Value("${system.validate.check:true}")  // 校验码开关，开发环境时可以关闭
    Boolean isValidateCode;

    /**
     * 通过用户名获取登录用户信息
     *
     * @param username 用户名
     * @return
     */
    @Override
    public Mono<UserDetails> findByUsername(final String username) {
        final LoginUserGetParamsDTO loginUserGetParamsDTO = new LoginUserGetParamsDTO();
        final ServerWebExchange swe = CorsWebFilter.serverWebExchangeThreadLocal.get();
        ServerHttpRequest request = swe.getRequest();
        final String microAppindex = request.getHeaders().getFirst(CoreConstant._MICRO_APPINDEX_KEY);
        final String userType = request.getHeaders().getFirst(CoreConstant._USER_TYPE_KEY);

        if (userType != null) {
            loginUserGetParamsDTO.getParamMap().put(CoreConstant._USER_TYPE_KEY, userType);
        } else {
            loginUserGetParamsDTO.getParamMap().remove(CoreConstant._USER_TYPE_KEY);
        }
        // 小程序登录后会获取用户名，并进行校验！

        final MultiValueMap<String, String> formData = CollectionUtils.toMultiValueMap(new HashMap<>());

        if (swe.getRequest().getURI().toString().endsWith("/auth/login")) {
            return swe.getFormData().doOnNext(formData::putAll)
                .then(Mono.defer(
                    () -> {
                        if (CollectionUtil.single().isEmpty(formData.getFirst("microServiceName"))) {
                            throw new AuthenticationServiceException("microServiceName不能为空！");
                        }
                        if (CollectionUtil.single().isEmpty(formData.getFirst(LoginFormField.PASSWORD.field()))) {
                            throw new AuthenticationServiceException(LoginFormField.PASSWORD.field() + "不能为空！");
                        }
                        loginUserGetParamsDTO.setMicroServiceName(formData.getFirst("microServiceName"));
                        loginUserGetParamsDTO.setPassword(formData.getFirst(LoginFormField.PASSWORD.field()));
                        // 微信小程序登录实现
                        if (microAppindex != null) {
                            final String weixinLoginCode = formData.getFirst(LoginFormField.PASSWORD.field());
                            loginUserGetParamsDTO.setUserName(weixinLoginCode);
                            loginUserGetParamsDTO.setMicroAppIndex(Integer.parseInt(microAppindex));
                            final LoginUserDetails userDetails = getUserByName(loginUserGetParamsDTO);
                            assert userDetails != null;
                            User.withUserDetails(userDetails).build();
                            return Mono.just(userDetails);
                        } else {
                            if (formData.getFirst("loginType") != null) {
                                loginUserGetParamsDTO.setLoginType(formData.getFirst("loginType"));
                            } else {
                                // 未定义登录方式，默认为后台页面登录
                                if (isValidateCode && (!checkValidateCode(swe, formData))) {
                                    return Mono.error(new ValidateCodeAuthFailException("验证码错误"));
                                }
                            }
                            loginUserGetParamsDTO.setUserName(username);

                            final Object otherFieldValue = formData.getFirst(LoginFormField.OTHER_FIELD_KEY.field());

                            if (!StringUtils.isEmpty(otherFieldValue)) {
                                loginUserGetParamsDTO.setParams(otherFieldValue.toString());
                            }
                            final LoginUserDetails userDetails = getUserByName(loginUserGetParamsDTO);
                            if (userDetails != null && CollectionUtil.single()
                                .isNotEmpty(userDetails.getUsername())) { // && StringUtils.equals(userDetails.getUsername(), username)
                                User.withUserDetails(userDetails).build();
                                return Mono.just(userDetails);
                            } else {
                                return Mono.error(new UsernameNotFoundException("User Not Found"));
                            }
                        }


                    }
                ));
        } else {
            loginUserGetParamsDTO.setUserName(username);
            final LoginUserDetails userDetails = getUserByName(loginUserGetParamsDTO);
            if (userDetails == null) {
                return Mono.empty();
            }
            return Mono.just(userDetails);
        }
    }

    /**
     * 校验验证码
     *
     * @param swe
     * @param formData
     * @return
     */
    private boolean checkValidateCode(ServerWebExchange swe, MultiValueMap<String, String> formData) {
        final String validateRedisKey = formData.getFirst(LoginFormField.VALIDATE_REDIS_KEY.field());
        final String validateCode = redisUtil.get(validateRedisKey);
        final String userValidateCode = formData.getFirst(LoginFormField.VALIDATE_CODE.field());
        if (CollectionUtil.single().isEmpty(validateCode)) {
            swe.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            // @todo 这里需要自定义异常，继承AuthenticationException
            return false;
        } else if (!validateCode.equalsIgnoreCase(userValidateCode)) {
            swe.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            return false;
        }
        redisUtil.remove(validateRedisKey);
        return true;
    }

    @Autowired
    IGetUserInfoFeignClient getUserInfoFeignClient;

    /**
     * 获取用户名
     *
     * @param loginUserGetParamsDTO 登录用户获取参数
     * @return
     */
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
