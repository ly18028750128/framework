package com.longyou.comm.scheduler;

import static org.cloud.constant.CoreConstant._BASIC64_TOKEN_USER_CACHE_KEY;
import static org.cloud.constant.CoreConstant._BASIC64_TOKEN_USER_SUCCESS_TOKEN_KEY;

import java.util.Set;
import org.cloud.constant.CoreConstant;
import org.cloud.core.redis.RedisUtil;
import org.cloud.scheduler.job.BaseQuartzJobBean;
import org.cloud.utils.RsaUtil;
import org.cloud.utils.SpringContextUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Component
public class RemoveExpireSuccessLoginUserCacheJob extends BaseQuartzJobBean {


    @Override
    protected void init() {
        this.jobName = "定时清空过期的登录用户缓存";
        jobData.put("description", "定时清空过期的登录用户缓存，默认60分种一次！");
        this.jobTime = "0 0 * * * ? ";  //10分钟刷新一次
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            final RedisUtil redisUtil = SpringContextUtil.getBean(RedisUtil.class);
            if (redisUtil == null) {
                return;
            }
            RedisTemplate redisTemplate = redisUtil.getRedisTemplate();
            Set<String> loginUserTokenKeys = redisTemplate.keys(_BASIC64_TOKEN_USER_SUCCESS_TOKEN_KEY + "*");
            for (String loginUserTokenKey : loginUserTokenKeys) {
                Set<String> keys = redisUtil.hashKeys(loginUserTokenKey);
                for (String key : keys) {
                    final Long expireTime = redisUtil.hashGet(loginUserTokenKey, key);
                    if (System.currentTimeMillis() > expireTime) {
                        redisUtil.remove(_BASIC64_TOKEN_USER_CACHE_KEY + key);
                        redisUtil.hashDel(loginUserTokenKey,key);
                    }
                }
            }
        } catch (Exception e) {
            new JobExecutionException(e);
        }
    }
}
