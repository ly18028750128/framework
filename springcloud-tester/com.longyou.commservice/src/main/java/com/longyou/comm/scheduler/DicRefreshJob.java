package com.longyou.comm.scheduler;

import com.longyou.comm.admin.service.IDicService;
import org.cloud.scheduler.job.BaseQuartzJobBean;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DicRefreshJob extends BaseQuartzJobBean {
    @Override
    protected void init() {
        this.jobName = "refresh sys data dic";
        jobData.put("description","刷新数据字典缓存，默认10分钟执行一次！");
        this.jobTime = 10 * 60;  //5分钟刷新一次
    }

    @Autowired
    IDicService dicService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            dicService.refreshCache();
        } catch (Exception e) {
            new JobExecutionException(e);
        }

    }
}
