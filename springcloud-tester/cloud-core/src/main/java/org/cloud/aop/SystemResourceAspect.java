package org.cloud.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.cloud.annotation.SystemResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Aspect
@Component
public class SystemResourceAspect {

    private Logger logger = LoggerFactory.getLogger(SystemResourceAspect.class);

    @Pointcut("@annotation(org.cloud.annotation.SystemResource)")
    public void systemResource(){};

    @Before("systemResource()")
    public void getSystemResource(JoinPoint joinPoint){
        SystemResource systemResource = joinPoint.getTarget().getClass().getAnnotation(SystemResource.class);

        logger.info(systemResource.authMethod().value());

    }
}

