package org.cloud.scheduler.job;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.result.DeleteResult;
import org.cloud.constant.CoreConstant;
import org.cloud.scheduler.service.QuartzService;
import org.cloud.utils.CommonUtil;
import org.cloud.utils.SpringContextUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;

// 定时删除日志，每天23点59分执行 0 59 23 * * ?
@Component
public class MongoDbLogJobCleaner extends BaseQuartzJobBean {

    Logger logger = LoggerFactory.getLogger(MongoDbLogJobCleaner.class);

    @Autowired
    QuartzService quartzService;

    @PostConstruct
    void registerJob() {
        this.jobName = "MongoDbLogJobCleaner";
        this.groupName = "logger";
        try {
            if (quartzService.isExists(this.jobName, this.groupName)) {
                return;
            }
            HashMap<String, String> jobData = new HashMap<String, String>();
            jobData.put("description", "System auto create!");
            // isStartNow参数一定要为false，不然会报错。
            quartzService.addJob(MongoDbLogJobCleaner.class, jobName, groupName, "0 59 23 * * ?", jobData, false);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        MongoTemplate mongoTemplate = SpringContextUtil.getBean(MongoTemplate.class);
        if (mongoTemplate != null) {
            Long expireDays = Long.parseLong(CommonUtil.single().getEnv("system.logger.mongodb.expire.days", "30"));  //默认保留30天的数据
            final String microServiceName = CommonUtil.single().getEnv("spring.application.name", "").toUpperCase();
            Query query = new Query(Criteria.where(CoreConstant.MongoDbLogConfig.CREATE_DATE_FIELD.value()).lt(new Date(System.currentTimeMillis() - expireDays * 24 * 60 * 60 * 1000)));
//            Query query = new Query(Criteria.where(CoreConstant.MongoDbLogConfig.CREATE_DATE_FIELD.value()).lt(new Date(System.currentTimeMillis())));
            DeleteResult deleteResult = mongoTemplate.remove(query, microServiceName + CoreConstant.MongoDbLogConfig.MONGODB_LOG_SUFFIX.value());

            logger.info("清除[" + microServiceName + CoreConstant.MongoDbLogConfig.MONGODB_LOG_SUFFIX.value() + "]日志成功，清理结果为::" + JSON.toJSONString(deleteResult));


        }
    }
}
