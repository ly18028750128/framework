package org.cloud.scheduler.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.cloud.scheduler.dto.JobTaskLog;
import org.cloud.utils.EnvUtil;
import org.quartz.JobExecutionContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.StringUtils;

/**
 * @Description: 在接口到达具体的目标即控制器方法之前获取方法的调用权限，可以在接口方法之前或者之后做Advice(增强)处理
 * @Author Emily
 * @Version: 1.0
 */
@Slf4j
public class DefaultJobTaskMethodInterceptor implements JobTaskLogCustomizer {

    private MongoTemplate mongoTemplate;
    private String microName;

    @Lazy
    public DefaultJobTaskMethodInterceptor(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        if (!StringUtils.hasLength(this.microName)) {
            this.microName = EnvUtil.single().getEnv("spring.application.name", "");
        }

        //开始时间
        long start = System.currentTimeMillis();
        String result = "success";
        String message = "success";
        try {
            Object response = invocation.proceed();
            return response;
        } catch (Throwable ex) {
            message = ex.getMessage();
            result = "fail";
            throw ex;
        } finally {
            final String finalMessage = message;
            final String finalResult = result;
            new Thread(() -> {
                JobExecutionContext context = (JobExecutionContext) invocation.getArguments()[0];
                mongoTemplate.save(new JobTaskLog(context, finalMessage, finalResult, (System.currentTimeMillis() - start), this.microName),
                    JOB_LOGS_COLLECTION);
            }).start();
        }
    }

    @Override
    public int getOrder() {
        return 100;
    }
}
