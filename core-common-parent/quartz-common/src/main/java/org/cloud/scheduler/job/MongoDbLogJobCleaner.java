package org.cloud.scheduler.job;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.result.DeleteResult;
import org.cloud.constant.CoreConstant;
import org.cloud.constant.CoreConstant.MongoDbLogConfig;
import org.cloud.utils.EnvUtil;
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

import java.util.Date;

// 定时删除日志，每天23点59分执行 0 59 23 * * ?
@Component
public class MongoDbLogJobCleaner extends BaseQuartzJobBean {

    Logger logger = LoggerFactory.getLogger(MongoDbLogJobCleaner.class);

    @Override
    protected void init() {
        this.jobName = "定时清除mongodb中的系统日志";
        jobData.put("description", "定时清除mongodb中的系统日志,默认每晚23:59分执行");
        this.jobTime = "0 59 23 * * ? ";
    }

    @Override
    protected void executeInternal(@NotNull JobExecutionContext context) throws JobExecutionException {
        final MongoTemplate mongoTemplate = SpringContextUtil.getBean(MongoTemplate.class);
        if (mongoTemplate != null) {
            long expireDays = Long.parseLong(EnvUtil.single().getEnv("system.logger.mongodb.expire.days", "30"));  //默认保留30天的数据
            final String microServiceName = EnvUtil.single().getEnv("spring.application.name", "").toUpperCase();
            final String activeProfile = EnvUtil.single().getEnv("spring.profiles.active", "").toUpperCase();
            String documentName = microServiceName + "_" + activeProfile + CoreConstant.MongoDbLogConfig.MONGODB_LOG_SUFFIX.value();
            try {
                Query query = new Query(Criteria.where(CoreConstant.MongoDbLogConfig.CREATE_DATE_FIELD.value())
                    .lt(new Date(System.currentTimeMillis() - expireDays * 24 * 60 * 60 * 1000)));
                DeleteResult deleteResult = mongoTemplate.remove(query, documentName);
                logger.info("清除[" + documentName + "]日志成功，清理结果为::" + JSON.toJSONString(deleteResult));
            } catch (Exception e) {
                logger.error("清除[" + documentName + "]日志失败,{}", e.getMessage());
            }

            try {
                documentName = microServiceName + MongoDbLogConfig.MONGODB_OPERATE_LOG_SUFFIX.value();
                Query query = new Query(Criteria.where(MongoDbLogConfig.CREATE_DATE_FIELD.value())
                    .lt(new Date(System.currentTimeMillis() - expireDays * 24 * 60 * 60 * 1000)));
                DeleteResult deleteResult = mongoTemplate.remove(query, documentName);
                logger.info("清除[" + documentName + "]操作日志成功，清理结果为::" + JSON.toJSONString(deleteResult));
            } catch (Exception e) {
                logger.error("清除[" + documentName + "]操作日志失败,{}", e.getMessage());
            }
        }
    }
}
