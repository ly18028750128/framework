package com.longyou.gateway.security;

import com.longyou.gateway.contants.LoginFormField;
import com.longyou.gateway.exception.ValidateCodeAuthFailException;
import com.longyou.gateway.service.feign.IGetUserInfoFeignClient;
import com.mongodb.BasicDBObject;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import org.cloud.annotation.AuthLog;
import org.cloud.constant.CoreConstant;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.CollectionUtil;
import org.cloud.utils.MD5Encoder;
import org.cloud.vo.LoginUserGetParamsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
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
        MultiValueMap<String, String> formData, ServerWebExchange swe) {
        return swe.getFormData().doOnNext(formData::putAll).then(Mono.defer(() -> {
            if (CollectionUtil.single().isEmpty(formData.getFirst("microServiceName"))) {
                throw new AuthenticationServiceException("microServiceName不能为空！");
            }
            if (CollectionUtil.single().isEmpty(formData.getFirst(LoginFormField.PASSWORD.field()))) {
                throw new AuthenticationServiceException(LoginFormField.PASSWORD.field() + "不能为空！");
            }

            loginUserGetParamsDTO.setMicroServiceName(formData.getFirst("microServiceName"));
            loginUserGetParamsDTO.setPassword(formData.getFirst(LoginFormField.PASSWORD.field()));

            final String targetMethodParams = Arrays.toString(
                new Object[]{username, formData.getFirst("microServiceName"), formData.getFirst("loginType"),
                    formData.getFirst(LoginFormField.VALIDATE_CODE.field()), microAppIndex});

            // 微信小程序登录实现
            if (microAppIndex != null) {
                final String wechatLoginCode = formData.getFirst(LoginFormField.PASSWORD.field());
                loginUserGetParamsDTO.setUserName(wechatLoginCode);
                loginUserGetParamsDTO.setMicroAppIndex(Integer.parseInt(microAppIndex));
                final LoginUserDetails userDetails = getUserByName(loginUserGetParamsDTO, swe);
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
                final LoginUserDetails userDetails = getUserByName(loginUserGetParamsDTO, swe);

                if (userDetails != null && CollectionUtil.single().isNotEmpty(userDetails.getUsername())) {
                    User.withUserDetails(userDetails).build();

                    new Thread(() -> {
                        saveLoginLog(username, userDetails, targetMethodParams, swe, true);
                    }).start();

                    return Mono.just(userDetails);
                } else {
                    new Thread(() -> {
                        saveLoginLog(username, userDetails, targetMethodParams, swe, false);
                    }).start();
                    return Mono.error(new UsernameNotFoundException("User Not Found"));
                }
            }


        }));
    }

    @Value("${spring.application.name:}")
    private String microName;

    @Autowired
    private MongoTemplate mongoTemplate;

    private void saveLoginLog(String username, LoginUserDetails userDetails, final String targetMethodParams, ServerWebExchange swe,
        final Boolean successFlag) {
        final BasicDBObject doc = new BasicDBObject();

        doc.append("success", successFlag);
        doc.append("microName", microName);
        doc.append("bizType", "gateway.login");
        doc.append("desc", "网关登录");
        doc.append("ip", this.getIpAddress(swe.getRequest()));
        doc.append("userName", username);
        doc.append("params", targetMethodParams);
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

    public String getIpAddress(ServerHttpRequest request) {
        String ip = request.getHeaders().getFirst("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.contains(",")) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeaders().getFirst("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = Objects.requireNonNull(request.getRemoteAddress()).toString();
        }

        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            return "127.0.0.1";
        }

        return ip;
    }

}
