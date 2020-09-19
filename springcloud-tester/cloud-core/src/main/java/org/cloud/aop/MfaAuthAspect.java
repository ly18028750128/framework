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
import org.cloud.context.RequestContext;
import org.cloud.context.RequestContextManager;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.mybatis.dynamic.DynamicSqlUtil;
import org.cloud.vo.DynamicSqlQueryParamsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
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

        if (CoreConstant.mfaAutoType.GOOGLE.equals(mfaAuthAnnotation.mfaAutoType())) {
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
        RequestContext currentRequestContext = RequestContextManager.single().getRequestContext();
        LoginUserDetails user = currentRequestContext.getUser();
        DynamicSqlQueryParamsVO dynamicSqlQueryParamsVO = new DynamicSqlQueryParamsVO();

        dynamicSqlQueryParamsVO.getParams().put("userId", user.getId());
        dynamicSqlQueryParamsVO.getParams().put("userId", user.getId());

        DynamicSqlUtil.single().listDataBySqlContext("select * t_frame_user_ref where attribute_name = and  user_id = #{userId,jdbcType=VARCHAR}", dynamicSqlQueryParamsVO);
    }

}

