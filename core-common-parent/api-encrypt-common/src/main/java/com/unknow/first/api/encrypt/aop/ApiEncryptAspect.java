package com.unknow.first.api.encrypt.aop;


import com.unknow.first.api.encrypt.annotation.ApiEncrypt;
import com.unknow.first.api.encrypt.util.ApiEncryptUtil;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.cloud.vo.CommonApiResult;

@Aspect
@Slf4j
public class ApiEncryptAspect {

    @Pointcut("@annotation(com.unknow.first.api.encrypt.annotation.ApiEncrypt)")
    public void apiEncryptPointcut() {

    }

    @AfterReturning(value = "apiEncryptPointcut()", returning = "result")
    public void after(JoinPoint joinPoint, CommonApiResult<Object> result) throws Throwable {
        Signature signature = joinPoint.getSignature();
        MethodSignature msg = (MethodSignature) signature;

        //获取注解标注的方法
        Method method = joinPoint.getTarget().getClass().getMethod(msg.getName(), msg.getParameterTypes());
        //通过方法获取注解
        final ApiEncrypt apiEncrypt = method.getAnnotation(ApiEncrypt.class);
        ApiEncryptUtil.encryptData(result, apiEncrypt.encryptType());
    }
}

