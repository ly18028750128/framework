package com.longyou.comm.scheduler;

import org.cloud.constant.CoreConstant;
import org.cloud.core.redis.RedisUtil;
import org.cloud.scheduler.job.BaseQuartzJobBean;
import org.cloud.utils.RsaUtil;
import org.cloud.utils.SpringContextUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;


@Component
public class RsaKeyRefreshJob extends BaseQuartzJobBean {


    @Override
    protected void init() {
        this.jobName = "定时刷新RSA秘钥";
        jobData.put("description", "定时刷新RSA秘钥，默认10分种一次！");
        this.jobTime = "0 0/10 * * * ? *";  //10分钟刷新一次
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            final RedisUtil redisUtil = SpringContextUtil.getBean(RedisUtil.class);
            if (redisUtil != null) {
                redisUtil.set(CoreConstant.RSA_KEYS_REDIS_KEY, RsaUtil.single().getRsaKey());
            }
        } catch (Exception e) {
            new JobExecutionException(e);
        }
    }


}
