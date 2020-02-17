package org.cloud.scheduler.job;

import org.cloud.scheduler.service.QuartzService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseQuartzJobBean extends QuartzJobBean {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected final Map<String, Object> jobData = new LinkedHashMap<String, Object>();
    protected String jobName = getClass().getSimpleName();
    protected String groupName = getClass().getPackage().getName();
    @NotNull
    protected Object jobTime;   // 定时任务的执行时间 如果是字符串，那么表示采用的是cron表达式，如果是整形，那么表示是按时间间隔,单位为秒
    @NotNull
    protected Integer jobTimes = -1; // 仅间隔定时任务有效，默认为无限次数

    @Autowired
    protected QuartzService quartzService;

    @PostConstruct
    void BaseQuartzJobBean() {
        jobData.put("description", "System auto create!");
        init();
        createJob();
    }

    protected abstract void init();


    protected void createJob() {
        try {
            if (!quartzService.isExists(jobName, groupName)) {  // 仅在启动时注册
                // isStartNow参数一定要为false，不然会报错。
                if (jobTime instanceof String) {
                    quartzService.addJob(getClass(), jobName, groupName, (String) jobTime, jobData, false);
                } else {
                    quartzService.addJob(getClass(), jobName, groupName, Integer.parseInt(jobTime.toString()), jobTimes, jobData, false);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
