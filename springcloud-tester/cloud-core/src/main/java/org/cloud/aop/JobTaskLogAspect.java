//package org.cloud.aop;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.stereotype.Component;
//
///**
// *
// */
//@Slf4j
//@Aspect
//@Component
//public class JobTaskLogAspect {
//
//    public JobTaskLogAspect(){
//        log.info("初始化定时任务日志切面");
//    }
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//    @Value("${spring.application.name:}")
//    private String microName;
//
//    @Value("${spring.application.group:}")
//    private String group;
//
//
//    @Pointcut("@annotation(org.cloud.annotation.scheduler.JobTaskLog)")
//    public void authLog() {
//    }
//
//    // 环绕切面持久化日志
//    @Around("authLog()")
//    public Object aroundMethod(ProceedingJoinPoint pjd) throws Throwable {
//        try {
//            log.info("{}", pjd.getThis());
//            Object res = pjd.proceed(pjd.getArgs());
//            return res;
//        } catch (Throwable e) {
//            throw e;
//        }
//    }
//
//
//}
