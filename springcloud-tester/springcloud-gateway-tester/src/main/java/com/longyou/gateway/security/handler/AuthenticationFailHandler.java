package com.longyou.gateway.security.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.longyou.gateway.constants.LoginFormField;
import com.longyou.gateway.security.UserLoginService;
import com.longyou.gateway.security.response.MessageCode;
import com.longyou.gateway.security.response.WsResponse;
import com.longyou.gateway.util.IPUtils;
import com.unknow.first.mail.manager.service.IEmailSenderService;
import com.unknow.first.mail.manager.vo.MailVO;
import com.unknow.first.mail.manager.vo.MailVO.EmailParams;
import java.util.Arrays;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.cloud.constant.CoreConstant;
import org.cloud.constant.LoginConstants.LoginError;
import org.cloud.core.redis.RedisUtil;
import org.cloud.utils.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Transactional(propagation = Propagation.NEVER)
@Slf4j
public class AuthenticationFailHandler implements ServerAuthenticationFailureHandler {

    @Autowired
    UserLoginService userLoginService;

    @Autowired
    RedisUtil redisUtil;

    @Value("${system.ip.login.error.limit:50}")
    Integer ipLoginErrorLimit;  // 同一个ip地址登录最大错误次数，超过这个次数将会被禁止登录30分钟

    @Value("${system.user.login.error.limit:5}")
    Integer userLoginErrorLimit;  // 同一个用户连续10次失败，超过这个次数将会被禁止登录30分钟

    @Value("${system.login.error.ipLockTime:3600}")
    Long ipLoginErrorLimitTime;  // 同一个用户登录最大错误次数，超过这个次数将会被禁止登录30分钟,

    @Value("${system.login.error.userLockTime:3600}")
    Long userLoginErrorLimitTime;  // 同一个用户登录最大错误次数，超过这个次数将会被禁止登录30分钟,

    @Value("${system.login.error.email.to:com_unknow_first@126.com}")
    String userLoginErrorEmailTo;  // 同一个用户登录最大错误次数，超过这个次数将会被禁止登录30分钟,


    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException e) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpResponse response = exchange.getResponse();

        final String ipAddress = IPUtils.getIpAddress(exchange.getRequest());
        final String ipAddressLockerCountKey = LoginError.IP_ERROR_COUNT_KEY.value + ipAddress;
        Integer ipLoginErrorCount = redisUtil.get(ipAddressLockerCountKey);
        ipLoginErrorCount = ipLoginErrorCount == null ? 1 : (ipLoginErrorCount + 1);
        String ipLockerKey = LoginError.IP_LOCK_KEY.value + ipAddress;
        Boolean ipIsLocked = redisUtil.get(ipLockerKey);

        ipIsLocked = (ipIsLocked != null && ipIsLocked);
        if (ipLoginErrorCount > ipLoginErrorLimit) {
            redisUtil.set(ipLockerKey, true, ipLoginErrorLimitTime);
            redisUtil.set(ipAddressLockerCountKey, 0);

            new Thread(() -> {
                EmailParams emailParams = new EmailParams();
                emailParams.getSubjectParams().put("ip", ipAddress);
                emailParams.getEmailParams().put("ip", ipAddress);
                emailParams.getEmailParams().put("unlockedDate", new Date(System.currentTimeMillis() + (userLoginErrorLimitTime * 1000)));
                IEmailSenderService emailSenderService = SpringContextUtil.getBean("emailSenderService");
                assert emailSenderService != null;
                try {
                    emailSenderService.sendEmail("IP_LOGIN_LOCKER_EMAIL", emailParams);
                } catch (Exception ex) {
                    log.error(e.getMessage(), ex);
                }
            }).start();
        } else if (!ipIsLocked) {
            redisUtil.set(ipAddressLockerCountKey, ipLoginErrorCount);
        }

        MultiValueMap<String, String> formData = exchange.getFormData().block();
        assert formData != null;
        final String username = formData.getFirst("username");
        final String loginType = formData.getFirst("loginType") == null ? "admin" : formData.getFirst("loginType");
        final String loginMicroServiceName = formData.getFirst("microServiceName");
        final String validateCode = formData.getFirst(LoginFormField.VALIDATE_CODE.field);
        final String microAppIndex = exchange.getRequest().getHeaders().getFirst(CoreConstant._MICRO_APPINDEX_KEY);
        final String userNameKey = (loginType == null ? "admin" : loginType) + ":" + username;
        final String userLoginCountKey = LoginError.USER_ERROR_COUNT_KEY.value + userNameKey;
        String loginParams = Arrays.toString(new Object[]{username, loginMicroServiceName, loginType, validateCode, microAppIndex});
        String userLockerKey = LoginError.USER_LOCK_KEY.value + userNameKey;
        Boolean userIsLocked = redisUtil.get(userLockerKey);
        Integer userLoginErrorCount = redisUtil.get(userLoginCountKey);
        userLoginErrorCount = userLoginErrorCount == null ? 1 : (userLoginErrorCount + 1);
        userIsLocked = (userIsLocked != null && userIsLocked);

        if (userLoginErrorCount > userLoginErrorLimit) {
            redisUtil.set(LoginError.USER_LOCK_KEY.value + userNameKey, true, userLoginErrorLimitTime);
            redisUtil.set(userLoginCountKey, 0);

            new Thread(() -> {

                EmailParams emailParams = new EmailParams();
                emailParams.getSubjectParams().put("userName", username);
                emailParams.getEmailParams().put("userName", username);
                emailParams.getEmailParams().put("ip", ipAddress);
                emailParams.getEmailParams().put("serviceName", loginMicroServiceName);
                emailParams.getEmailParams().put("unlockedDate", new Date(System.currentTimeMillis() + (userLoginErrorLimitTime * 1000)));
                try {
                    IEmailSenderService emailSenderService = SpringContextUtil.getBean("emailSenderService");
                    assert emailSenderService != null;
                    emailSenderService.sendEmail("USER_LOGIN_LOCKER_EMAIL", emailParams);
                } catch (Exception ex) {
                    log.error(e.getMessage(), ex);
                }
            }).start();
        } else if (!userIsLocked) {
            redisUtil.set(userLoginCountKey, userLoginErrorCount);
        }
        new Thread(() -> {
            userLoginService.saveLoginLog(username, null, loginParams, exchange, "Fail", e.getMessage());
        }).start();
        //设置headers
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        //设置body
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        WsResponse<?> wsResponse = WsResponse.failure(MessageCode.COMMON_AUTHORIZED_FAILURE, e.getMessage());
        byte[] dataBytes = {};
        try {
            ObjectMapper mapper = new ObjectMapper();
            dataBytes = mapper.writeValueAsBytes(wsResponse);
        } catch (Exception ex) {
            log.error(e.getMessage(), e);
        }
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(dataBytes);
        return response.writeWith(Mono.just(bodyDataBuffer));
    }


}
