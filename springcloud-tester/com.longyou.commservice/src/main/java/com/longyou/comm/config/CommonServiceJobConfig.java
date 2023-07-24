package com.longyou.comm.config;

import com.unknow.first.imexport.feign.ImexportTaskFeignClient;
import com.unknow.first.imexport.job.ImexportTaskRemoteJob;
import org.cloud.core.redis.RedisUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonServiceJobConfig {

    @Bean
    public ImexportTaskRemoteJob imexportTaskRemoteJob( ImexportTaskFeignClient imexportTaskFeignClient,RedisUtil redisUtil) {
        return new ImexportTaskRemoteJob(imexportTaskFeignClient, redisUtil);
    }
}
