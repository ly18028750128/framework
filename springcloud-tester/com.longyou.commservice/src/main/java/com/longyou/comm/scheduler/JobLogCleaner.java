package com.longyou.comm.scheduler;

import static org.cloud.scheduler.interceptor.JobTaskLogCustomizer.JOB_LOGS_COLLECTION;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.result.DeleteResult;
import java.util.Date;
import org.cloud.constant.CoreConstant.MongoDbLogConfig;
import org.cloud.scheduler.job.BaseQuartzJobBean;
import org.cloud.utils.CommonUtil;
import org.cloud.utils.SpringContextUtil;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

// 定时删除日志，每天23点59分执行 0 59 23 * * ?
@Component
public class JobLogCleaner extends BaseQuartzJobBean {

    Logger logger = LoggerFactory.getLogger(JobLogCleaner.class);

    @Override
    protected void init() {
        this.jobName = "定时清除mongodb中的定时任务日志";
        jobData.put("description", "定时清除mongodb中的定时任务日志,默认每晚23:55分执行");
        this.jobTime = "0 55 23 * * ? ";
    }

    @Override
    protected void executeInternal(@NotNull JobExecutionContext context) throws JobExecutionException {
        final MongoTemplate mongoTemplate = SpringContextUtil.getBean(MongoTemplate.class);
        if (mongoTemplate != null) {
            long expireDays = Long.parseLong(CommonUtil.single().getEnv("system.logger.jobLogs.expire.days", "7"));  //默认保留7天的数据
            try {
                Query query = new Query(Criteria.where(MongoDbLogConfig.CREATE_DATE_FIELD.value()).lt(new Date(System.currentTimeMillis() - expireDays * 24 * 60 * 60 * 1000)));
                DeleteResult deleteResult = mongoTemplate.remove(query, JOB_LOGS_COLLECTION);
                logger.info("清除[" + JOB_LOGS_COLLECTION + "]日志成功，清理结果为::" + JSON.toJSONString(deleteResult));
            } catch (Exception e) {
                logger.error("清除[" + JOB_LOGS_COLLECTION + "]日志失败,{}", e.getMessage());
            }
        }
    }
}
