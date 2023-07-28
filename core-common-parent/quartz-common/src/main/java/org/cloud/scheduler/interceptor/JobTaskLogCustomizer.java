package org.cloud.scheduler.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.core.Ordered;

/**
 * @Description: Mybatis埋点扩展点接口MybatisCustomizer，AOP切面会根据优先级选择优先级最高的拦截器
 * @Author: Emily
 * @create: 2022/2/12
 */
public interface JobTaskLogCustomizer extends MethodInterceptor, Ordered {
    String JOB_LOGS_COLLECTION = "jobLogs_collection";
}
