package org.cloud.logger;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import com.mongodb.BasicDBObject;
import org.cloud.constant.CoreConstant;
import org.cloud.utils.CommonUtil;
import org.cloud.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;


public class MongoDBAppender extends
        UnsynchronizedAppenderBase<ILoggingEvent> {

    private static MongoTemplate mongoTemplate;
    private Logger logger = LoggerFactory.getLogger(MongoDBAppender.class);

    @Override
    protected void append(ILoggingEvent eventObject) {
        if (mongoTemplate == null) {
            mongoTemplate = SpringContextUtil.getBean(MongoTemplate.class);
            return;
        }
        String microServiceName = CommonUtil.single().getEnv("spring.application.name", "").toUpperCase();
        final BasicDBObject doc = new BasicDBObject();
        doc.append("level", eventObject.getLevel().toString());
        doc.append("logger", eventObject.getLoggerName());
        doc.append("thread", eventObject.getThreadName());
        doc.append("message", eventObject.getFormattedMessage());
        doc.append(CoreConstant.MongoDbLogConfig.CREATE_DATE_FIELD.value(), new Date(eventObject.getTimeStamp()));
        doc.append("microServiceName", microServiceName);
        mongoTemplate.insert(doc, microServiceName + CoreConstant.MongoDbLogConfig.MONGODB_LOG_SUFFIX.value());
    }
}
