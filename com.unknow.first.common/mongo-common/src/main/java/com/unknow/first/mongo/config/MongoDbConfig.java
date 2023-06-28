package com.unknow.first.mongo.config;

import com.unknow.first.mongo.utils.MongoDBUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

/**
 * @author Administrator
 */
@Configuration
public class MongoDbConfig {

    @Bean
    public MongoDBUtil mongoDBUtil(MongoTemplate mongoTemplate, GridFsTemplate gridFsTemplate) {
        return new MongoDBUtil(mongoTemplate,gridFsTemplate);
    }
}
