package org.cloud.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.context.RequestContextManager;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Set;

@Aspect
@Component
@Slf4j
public class SystemResourceAspect {
    @Value("${spring.application.noGroupName:}")
    private String microName;
    @Autowired
    RedisUtil redisUtil;

    @Pointcut("@annotation(org.cloud.annotation.SystemResource)")
    public void systemResource() {
    }

    @Around("systemResource()")
    public Object getSystemResource(ProceedingJoinPoint joinPoint) throws Throwable {

        //用的最多通知的签名
        Signature signature = joinPoint.getSignature();
        MethodSignature msg = (MethodSignature) signature;
        Object target = joinPoint.getTarget();

        final SystemResource classResourceAnnotation = target.getClass().getAnnotation(SystemResource.class);

        //获取注解标注的方法
        Method method = target.getClass().getMethod(msg.getName(), msg.getParameterTypes());
        //通过方法获取注解
        final SystemResource systemResource = method.getAnnotation(SystemResource.class);

        // 如果是不校验，那么全部用户均可以通过
        if (!systemResource.authMethod().equals(CoreConstant.AuthMethod.NOAUTH)) {
            LoginUserDetails loginUserDetails = RequestContextManager.single().getRequestContext().getUser();
            if (loginUserDetails == null) {
                throw HttpClientErrorException.create(HttpStatus.UNAUTHORIZED, "请登录！", null, null, Charset.forName("utf8"));
            }
            // 校验功能权限！
            if (systemResource.authMethod().equals(CoreConstant.AuthMethod.BYUSERPERMISSION)) {
                Set<String> userFunctions = redisUtil.hashGet(CoreConstant.USER_LOGIN_SUCCESS_CACHE_KEY + loginUserDetails.getId(), CoreConstant.UserCacheKey.FUNCTION.value());
                final String functionSetStr = microName + CoreConstant._FUNCTION_SPLIT_STR + classResourceAnnotation.path() + CoreConstant._FUNCTION_SPLIT_STR + systemResource.value();
                if (userFunctions == null || userFunctions.isEmpty() || !userFunctions.contains(functionSetStr)) {
                    log.error(loginUserDetails.getUsername() + ",正在非法的请求：" + functionSetStr);
                    throw HttpClientErrorException.create(HttpStatus.UNAUTHORIZED, "没有操作权限！", null, null, Charset.forName("utf8"));
                }
            }
        }
        return joinPoint.proceed();
    }
}

