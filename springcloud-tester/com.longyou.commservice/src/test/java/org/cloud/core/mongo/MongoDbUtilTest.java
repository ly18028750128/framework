package org.cloud.core.mongo;

import com.longyou.comm.starter.CommonServiceApplication;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.cloud.mongo.AppLogger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CommonServiceApplication.class}, properties = "classpath:testYml.yml")
//@ActiveProfiles(value = "dev")
public class MongoDbUtilTest {

    Logger logger = LoggerFactory.getLogger(MongoDbUtilTest.class);



    @Autowired
    private MongoTemplate mongoTemplate;


    @Test
    public void testInsert() {

        Collection<Map<String,Object>> testValues = new ArrayList<>();

        for(int i=0;i<100;i++){
            AppLogger appLogger = new AppLogger();
            appLogger.setClassName("test"+i);
            appLogger.setMethod("testMethod");
            appLogger.setParams(new String[]{"a","b"});
            appLogger.setExceptionStr("测试错误");
            mongoTemplate.insert(appLogger,"appLoggerCollection");
        }

        MongoCollection<Document> appLoggerCollection = mongoTemplate.getCollection("appLoggerCollection");


        Pattern pattern = Pattern.compile("^test1.+$",Pattern.CASE_INSENSITIVE);
        Query query = new Query(Criteria.where("className").regex(pattern));
        List<AppLogger> findList = mongoTemplate.find(query,AppLogger.class,"appLoggerCollection");



        Assert.assertEquals(findList.size()>0,true);


//        TFrameDataInterfaceParams dataInterfaceParams = new TFrameDataInterfaceParams();

//        dataInterfaceParams.setDataCode("1232");


//        mongoTemplate.insert(dataInterfaceParams,"dataInterfaceParamsCollection");

//        mongoTemplate.insertAll(testValues);
    }


}