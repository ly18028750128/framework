package com.longyou.comm.scheduler;

import static org.cloud.utils.EccUtil.ECC_KEYS_REDIS_KEY;

import org.cloud.core.redis.RedisUtil;
import org.cloud.scheduler.job.BaseQuartzJobBean;
import org.cloud.utils.EccUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;


@Component
public class EccKeyRefreshJob extends BaseQuartzJobBean {


    @Override
    protected void init() {
        this.jobName = "定时刷新ECC秘钥";
        jobData.put("description", "定时刷新ECC秘钥，默认60分种一次！");
        this.jobTime = "0 0 * * * ? *";  //10分钟刷新一次
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            RedisUtil.single().set(ECC_KEYS_REDIS_KEY, EccUtil.single().getEccKey());
        } catch (Exception e) {
            new JobExecutionException(e);
        }
    }


}
