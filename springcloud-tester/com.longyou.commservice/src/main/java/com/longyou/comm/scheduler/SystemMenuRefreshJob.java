package com.longyou.comm.scheduler;

import com.longyou.comm.admin.service.IDicService;
import com.longyou.comm.admin.service.IMenuService;
import org.cloud.scheduler.job.BaseQuartzJobBean;
import org.cloud.utils.SpringContextUtil;
import org.cloud.utils.SystemStringUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.annotation.Lazy;
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
            final IMenuService iMenuService = SpringContextUtil.getBean(IMenuService.class);
            if (iMenuService != null) {
                iMenuService.listAllTreeMenu(null,5);   //查询并刷新缓存
            }
        } catch (Exception e) {
            new JobExecutionException(e);
        }
    }
}
