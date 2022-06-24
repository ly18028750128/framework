package com.longyou.gateway.security;

import com.longyou.gateway.constants.GatewayConstants.LoginException;
import com.longyou.gateway.constants.LoginFormField;
import com.longyou.gateway.exception.ValidateCodeAuthFailException;
import com.longyou.gateway.service.feign.IGetUserInfoFeignClient;
import com.longyou.gateway.util.IPUtils;
import com.mongodb.BasicDBObject;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import lombok.SneakyThrows;
import org.cloud.constant.CoreConstant;
import org.cloud.constant.LoginConstants.LoginError;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.exception.BusinessException;
import org.cloud.utils.CollectionUtil;
import org.cloud.utils.MD5Encoder;
import org.cloud.vo.LoginUserGetParamsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Service
public class UserLoginServiceImpl implements UserLoginService {


    @Autowired
    RedisUtil redisUtil;

    @Value("${system.validate.check:true}")  // 校验码开关，开发环境时可以关闭
    Boolean isValidateCode;

    @Override
    public Mono<UserDetails> userLogin(String username, LoginUserGetParamsDTO loginUserGetParamsDTO, String microAppIndex,
        ServerWebExchange swe) {

        final MultiValueMap<String, String> formData = CollectionUtils.toMultiValueMap(new HashMap<>());
        String ipAddressLockerCountKey = LoginError.IP_LOCK_KEY.value + IPUtils.getIpAddress(swe.getRequest());
        Boolean ipIsLocker = redisUtil.get(ipAddressLockerCountKey);
        if (ipIsLocker != null && ipIsLocker) {
            return Mono.error(new UsernameNotFoundException(LoginException.ERROR_35.i18nValue));
        }
        return swe.getFormData().doOnNext(formData::putAll).then(Mono.defer(() -> {
            final String loginMicroServiceName = formData.getFirst("microServiceName");
            final String loginType = formData.getFirst("loginType");
            final String validateCode = formData.getFirst(LoginFormField.VALIDATE_CODE.field);
            final String userNameKey = (loginType == null ? "admin" : loginType) + ":" + username;

            if (CollectionUtil.single().isEmpty(loginMicroServiceName)) {
                throw new AuthenticationServiceException(LoginException.ERROR_10.i18nValue);
            }
            if (CollectionUtil.single().isEmpty(formData.getFirst(LoginFormField.PASSWORD.field))) {
                throw new AuthenticationServiceException(LoginException.ERROR_20.i18nValue);
            }
            final String userIsLockedKey = LoginError.USER_LOCK_KEY.value + userNameKey;
            Boolean userIsLocked = redisUtil.get(userIsLockedKey);
            if (userIsLocked != null && userIsLocked) {
                return Mono.error(new UsernameNotFoundException(LoginException.ERROR_30.i18nValue));
            }
            loginUserGetParamsDTO.setMicroServiceName(loginMicroServiceName);
            loginUserGetParamsDTO.setPassword(formData.getFirst(LoginFormField.PASSWORD.field));

            if (loginType != null) {
                loginUserGetParamsDTO.setLoginType(loginType);
            } else if (isValidateCode && (!checkValidateCode(swe, formData))) { // 未定义登录方式，默认为后台页面登录,那么要校验验证码
                return Mono.error(new ValidateCodeAuthFailException(LoginException.ERROR_40.i18nValue));
            }

            // 微信小程序登录实现
            if (microAppIndex != null) {
                final String wechatLoginCode = formData.getFirst(LoginFormField.PASSWORD.field);
                loginUserGetParamsDTO.setUserName(wechatLoginCode);
                loginUserGetParamsDTO.setMicroAppIndex(Integer.parseInt(microAppIndex));
            } else {
                loginUserGetParamsDTO.setUserName(username);
                final Object otherFieldValue = formData.getFirst(LoginFormField.OTHER_FIELD_KEY.field);
                if (!StringUtils.isEmpty(otherFieldValue)) {
                    loginUserGetParamsDTO.setParams(otherFieldValue.toString());
                }
            }

            final LoginUserDetails userDetails = getUserByName(loginUserGetParamsDTO, swe);
            final String targetMethodParams = Arrays.toString(
                new Object[]{username, loginMicroServiceName, loginType, validateCode, microAppIndex});

            if (userDetails != null && CollectionUtil.single().isNotEmpty(userDetails.getUsername())) {
                User.withUserDetails(userDetails).build();

                new Thread(() -> {
                    saveLoginLog(username, userDetails, targetMethodParams, swe, "getUserSuccess", "用户获取成功，密码校验中!");
                }).start();

                return Mono.just(userDetails);
            } else {
                new Thread(() -> {
                    saveLoginLog(username, userDetails, targetMethodParams, swe, "Fail", "用户不存在！");
                }).start();
                return Mono.error(new UsernameNotFoundException("User Not Found"));
            }

        }));
    }

    @Value("${spring.application.name:}")
    private String microName;

    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    @Async
    public void saveLoginLog(String username, LoginUserDetails userDetails, final String targetMethodParams, ServerWebExchange swe,
        final String successFlag, String message) {
        final BasicDBObject doc = new BasicDBObject();

        doc.append("success", successFlag);
        doc.append("microName", microName);
        doc.append("bizType", "gateway.login");
        doc.append("desc", "网关登录");
        doc.append("ip", IPUtils.getIpAddress(swe.getRequest()));
        doc.append("userName", username);
        doc.append("params", targetMethodParams);
        doc.append("message", message);
        if (userDetails != null) {
            doc.append("userId", userDetails.getId());
        }
        doc.append(CoreConstant.MongoDbLogConfig.CREATE_DATE_FIELD.value(), new Date());
        mongoTemplate.insert(doc, microName + CoreConstant.MongoDbLogConfig.MONGODB_OPERATE_LOG_SUFFIX.value());
    }

    /**
     * 校验验证码
     *
     * @param swe
     * @param formData
     * @return
     */
    private boolean checkValidateCode(ServerWebExchange swe, MultiValueMap<String, String> formData) {
        final String validateRedisKey = formData.getFirst(LoginFormField.VALIDATE_REDIS_KEY.field);
        final String validateCode = redisUtil.get(validateRedisKey);
        final String userValidateCode = formData.getFirst(LoginFormField.VALIDATE_CODE.field);
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

    @Lazy
    @Autowired
    IGetUserInfoFeignClient getUserInfoFeignClient;

    /**
     * 获取用户名
     *
     * @param loginUserGetParamsDTO 登录用户获取参数
     * @return
     */
    @Override
    public LoginUserDetails getUserByName(LoginUserGetParamsDTO loginUserGetParamsDTO, ServerWebExchange swe) {
        if (swe == null) {
            return null;
        }
        final String token = swe.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        // 如果带了token那么从缓存中获取数据
        if (token != null && !"0".equals(token) && token.length() > 15) {
            return redisUtil.get(CoreConstant._BASIC64_TOKEN_USER_CACHE_KEY + MD5Encoder.encode(token));
        } else {
            return getUserInfoFeignClient.getUserInfo(loginUserGetParamsDTO);
        }
    }
}
