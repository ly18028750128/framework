package com.unknow.first.mongo.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.unknow.first.mongo.utils.MongoDBUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import static com.mongodb.connection.SocketSettings.builder;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Administrator
 */
@Configuration
@ConditionalOnProperty(prefix = "system.mongo", name = "enabled", matchIfMissing = true)
public class MongoDbConfig {

    @Bean
    public MongoDBUtil mongoDBUtil(MongoTemplate mongoTemplate, GridFsTemplate gridFsTemplate) {
        return new MongoDBUtil(mongoTemplate, gridFsTemplate);
    }
}
