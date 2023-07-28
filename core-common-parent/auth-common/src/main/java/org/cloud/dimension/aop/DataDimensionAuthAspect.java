package org.cloud.dimension.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.cloud.dimension.annotation.DataDimensionAuth;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@Order(9)
public class DataDimensionAuthAspect {

  private static ThreadLocal<Boolean> dataDimensionOpenThreadLocal = new ThreadLocal<>();

  public static boolean isOpen() {
    if (dataDimensionOpenThreadLocal.get() == null) {
      return false;
    }
    return dataDimensionOpenThreadLocal.get();
  }

  @Pointcut("@annotation(org.cloud.dimension.annotation.DataDimensionAuth)")
  public void dataDimensionAuth() {
  }

  @Around("dataDimensionAuth()")
  public Object dataDimensionOpen(ProceedingJoinPoint joinPoint) throws Throwable {
    try {
      MethodSignature ms = (MethodSignature) joinPoint.getSignature();
      DataDimensionAuth dataDimensionAuth = ms.getMethod().getAnnotation(DataDimensionAuth.class);
      if (dataDimensionAuth.open()) {
//        log.info("方法[{}]需要校验数据权限", ms.getMethod());
      }
      dataDimensionOpenThreadLocal.set(dataDimensionAuth.open());
      return joinPoint.proceed();
    } finally {
      dataDimensionOpenThreadLocal.remove();
    }

  }

}

