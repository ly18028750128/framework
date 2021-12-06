package com.longyou.comm.scheduler;

import static com.longyou.comm.admin.service.IMenuService._ALL_MENUS_CACHE_KEY;

import com.longyou.comm.admin.service.IMenuService;
import org.cloud.core.redis.RedisUtil;
import org.cloud.scheduler.job.BaseQuartzJobBean;
import org.cloud.utils.SpringContextUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class SystemMenuRefreshJob extends BaseQuartzJobBean {

    @Override
    protected void init() {
        this.jobName = "定时刷新系统菜单缓存";
        jobData.put("description", "定时刷新系统菜单缓存,默认10分钟执行一次");
        this.jobTime = "0 0/10 * * * ? *";  //10分钟刷新一次
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            final RedisUtil redisUtil = SpringContextUtil.getBean(RedisUtil.class);
            final IMenuService menuService = SpringContextUtil.getBean(IMenuService.class);
            if (redisUtil != null && menuService != null) {
                redisUtil.set(_ALL_MENUS_CACHE_KEY, menuService.listAllTreeMenu(null, 5, false), 0L);
            }
            //查询并刷新缓存
        } catch (Exception e) {
             throw new JobExecutionException(e);
        }
    }
}
