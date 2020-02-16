package org.cloud.scheduler.test;

import com.alibaba.fastjson.JSON;
import org.cloud.scheduler.job.BaseQuartzJobBean;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component("JustTest.testJob")
public class TestJob extends BaseQuartzJobBean {

    private final Logger logger = LoggerFactory.getLogger(TestJob.class);

    @Override
    protected void init() {
        this.jobTime = 10;
//        this.jobTimes = 10;
    }

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        logger.info(new Date() + "::" + context.getJobDetail().getKey().getName() + ":" + context.getJobDetail().getJobClass() + " 开始执行！");
        logger.info("context.getTrigger().getJobDataMap()=" + JSON.toJSONString(context.getTrigger().getJobDataMap()));
        logger.info("context.getJobDetail().getJobDataMap()=" + JSON.toJSONString(context.getJobDetail().getJobDataMap()));
        logger.info(new Date() + "::" + context.getJobDetail().getKey().getGroup() + ":" + context.getJobDetail().getJobClass() + " 结束执行！");
    }
}
