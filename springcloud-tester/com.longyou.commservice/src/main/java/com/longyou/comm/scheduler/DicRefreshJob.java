package com.longyou.comm.scheduler;

import com.longyou.comm.admin.service.IDicService;
import org.cloud.scheduler.job.BaseQuartzJobBean;
import org.cloud.utils.SpringContextUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DicRefreshJob extends BaseQuartzJobBean {
    @Override
    protected void init() {
        jobData.put("description", "system dic refresh on per 10 minutes!"); // 暂时不支持中文！
        this.jobTime = "0 0/10 * * * ? *";  //10分钟刷新一次
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            final IDicService dicService = SpringContextUtil.getBean(IDicService.class);
            ;
            if (dicService != null) {
                dicService.refreshCache();
            }
        } catch (Exception e) {
            new JobExecutionException(e);
        }
    }
}
