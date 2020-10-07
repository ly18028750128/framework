package org.cloud.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.cloud.annotation.MfaAuth;
import org.cloud.constant.CoreConstant;
import org.cloud.core.redis.RedisUtil;
import org.cloud.utils.GoogleAuthenticatorUtil;
import org.cloud.utils.SystemDicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
@Order(10)
public class MfaAuthAspect {

    @Value("${spring.application.noGroupName:}")
    private String microName;
    @Autowired
    RedisUtil redisUtil;

    @Pointcut("@annotation(org.cloud.annotation.MfaAuth)")
    public void mfaAuth() {
    }

    @Around("mfaAuth()")
    public Object mfaAuthCheck(ProceedingJoinPoint joinPoint) throws Throwable {

        //用的最多通知的签名
        Signature signature = joinPoint.getSignature();
        MethodSignature msg = (MethodSignature) signature;
        Object target = joinPoint.getTarget();
        Method method = target.getClass().getMethod(msg.getName(), msg.getParameterTypes());
        final MfaAuth mfaAuthAnnotation = method.getAnnotation(MfaAuth.class);

        // 如果不校验双因子验证那么直接不拦截，此处为url强校验开关，每次都要校验。
        if ("false".equals(SystemDicUtil.single().getDicItem("systemConfig", "isMfaVerify"))) {
            return joinPoint.proceed();
        }

        if (CoreConstant.MfaAuthType.GOOGLE.equals(mfaAuthAnnotation.mfaAuthType())) {
                checkGoogleValidCode();
        } else {
            // todo 其它方式暂时不处理
        }
        return joinPoint.proceed();
    }


    /**
     * 校验当前用户的谷歌验证码是否正确
     */
    private void checkGoogleValidCode() throws Exception {
        String googleSecret = GoogleAuthenticatorUtil.single().getCurrentUserVerifyKey();
        GoogleAuthenticatorUtil.single().checkGoogleVerifyCode(googleSecret);
    }

}

