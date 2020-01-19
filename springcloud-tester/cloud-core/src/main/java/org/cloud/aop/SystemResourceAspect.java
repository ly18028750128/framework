package org.cloud.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.context.RequestContextManager;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.model.TFrameRole;
import org.cloud.model.TFrameRoleResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

@Aspect
@Component
public class SystemResourceAspect {
    private Logger logger = LoggerFactory.getLogger(SystemResourceAspect.class);

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
        //获取注解标注的方法
        Method method = target.getClass().getMethod(msg.getName(), msg.getParameterTypes());
        //通过方法获取注解
        SystemResource systemResource = method.getAnnotation(SystemResource.class);

        logger.info(systemResource.authMethod().value());

        // 如果是不校验，那么全部用户均可以通过
        if (systemResource.authMethod().equals(CoreConstant.AuthMethod.NOAUTH)) {
            return joinPoint.proceed();
        } else {
            LoginUserDetails loginUserDetails = RequestContextManager.single().getRequestContext().getUser();
            if (loginUserDetails == null) {
                throw HttpClientErrorException.create(HttpStatus.UNAUTHORIZED, "请登录！", null, null, Charset.forName("utf8"));
            }
            // 校验数据权限！
            if (systemResource.authMethod().equals(CoreConstant.AuthMethod.BYUSERPERMISSION)) {
                Set<String> userFunctions = redisUtil.hashGet(CoreConstant.USER_LOGIN_SUCCESS_CACHE_KEY + loginUserDetails.getId(),CoreConstant.UserCacheKey.FUNCTION.value());
                if (userFunctions == null || userFunctions.isEmpty() || !userFunctions.contains(systemResource.value()))
                    throw HttpClientErrorException.create(HttpStatus.UNAUTHORIZED, "没有操作权限！", null, null, Charset.forName("utf8"));
            }
        }
        return joinPoint.proceed();
    }


}

