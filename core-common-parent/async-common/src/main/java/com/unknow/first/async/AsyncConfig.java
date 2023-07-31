package com.unknow.first.async;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
@Slf4j
@ConditionalOnProperty(prefix = "system.async", name = "enabled", matchIfMissing = true)
@ConfigurationProperties(prefix = "system.async")
public class AsyncConfig extends AsyncConfigurerSupport {

    @Setter
    private Integer corePoolSize=20;
    @Setter
    private Integer maxPoolSize=200;
    @Setter
    private Integer queueCapacity=20;
    @Setter
    private String threadNamePrefix="UnknownAsyncExecutor-";


    public AsyncConfig() {
        super();
        log.info("AsyncConfig is init");
    }

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix(threadNamePrefix);
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new MyAsyncUncaughtExceptionHandler();
    }

    static class MyAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable ex, Method method, @NotNull Object... params) {
            log.error("class:{},method:{},type:{},exception:{}", method.getDeclaringClass().getName(), method.getName(),
                ex.getClass().getName(), ex.getMessage());
        }
    }

}