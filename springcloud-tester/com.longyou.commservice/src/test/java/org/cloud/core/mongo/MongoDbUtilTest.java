package org.cloud.core.mongo;

import com.longyou.comm.starter.CommonServiceApplication;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.cloud.mongo.AppLogger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;
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

        Collection<Map<String, Object>> testValues = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            AppLogger appLogger = new AppLogger();
            appLogger.setClassName("test" + i);
            appLogger.setMethod("testMethod");
            appLogger.setParams(new String[]{"a", "b"});
            appLogger.setExceptionStr("测试错误");
            mongoTemplate.insert(appLogger, "appLoggerCollection");
        }

        MongoCollection<Document> appLoggerCollection = mongoTemplate.getCollection("appLoggerCollection");


        Pattern pattern = Pattern.compile("^test1.+$", Pattern.CASE_INSENSITIVE);
        Query query = new Query(Criteria.where("className").regex(pattern));
        List<AppLogger> findList = mongoTemplate.find(query, AppLogger.class, "appLoggerCollection");


        Assert.assertEquals(findList.size() > 0, true);

    }

    @Test
    public void testDelete() throws Exception {
        Query query = new Query(Criteria.where("createDate").lt(new SimpleDateFormat("yyyyMMdd").parse("20200213")));
        mongoTemplate.findAllAndRemove(query, "LONGYOUXGSIXTEEN_logbackLogCollection");
    }

    @Test
    public void testUpdate() throws Exception {
        Query query = new Query(Criteria.where("_id").is(new ObjectId("5e42898437a34810e4a8c971")));
        Update update = new Update();
        update.set("updateDate", new Date());
        mongoTemplate.findAndModify(query, update, FindAndModifyOptions.options().remove(true), BasicDBObject.class, "LONGYOUSPRING-GATEWAY_logbackLogCollection");
    }

    @Test
    @After
    public void testQuery() throws Exception {
        Pattern pattern = Pattern.compile("^test1.+$", Pattern.CASE_INSENSITIVE);
        Query query = new Query(Criteria.where("className").regex(pattern));
        List<AppLogger> findList = mongoTemplate.find(query, AppLogger.class, "appLoggerCollection");
        Assert.assertEquals(findList.size() > 0, true);
    }

    @Autowired
    GridFsTemplate gridFsTemplate;
    @Test
    public void saveFile() throws Exception{

//

        ObjectId objectId = gridFsTemplate.store(new FileInputStream("D:\\messages.xml"),"messageName");

        GridFSFile gridFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(objectId)));


        gridFile = gridFsTemplate.findOne(Query.query(Criteria.where("filename").is("messageName")));


        gridFile = null;
    }

}