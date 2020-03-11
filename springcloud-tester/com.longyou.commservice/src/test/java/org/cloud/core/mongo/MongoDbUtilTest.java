package org.cloud.core.mongo;

import com.longyou.comm.starter.CommonServiceApplication;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.BasicBSONObject;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.cloud.constant.CoreConstant;
import org.cloud.mongo.AppLogger;
import org.cloud.mongo.DataInterFaceVO;
import org.cloud.mongo.MongoEnumVO;
import org.cloud.mongo.MongoQueryParam;
import org.cloud.utils.mongo.MongoDBUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Sort;
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
        mongoTemplate.updateMulti(query, update,  "LONGYOUSPRING-GATEWAY_logbackLogCollection");
    }

    @Test
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
        ObjectId objectId = gridFsTemplate.store(new FileInputStream("D:\\messages.xml"),"messageName");
        GridFSFile gridFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(objectId)));
        gridFile = gridFsTemplate.findOne(Query.query(Criteria.where("filename").is("messageName")));
        gridFile = null;
    }

    @Test
    public void saveInterfaceVO() throws Exception{
        List<DataInterFaceVO> dataInterFaceVOS = new ArrayList<>();
        for(int i=0;i<126;i++){
            DataInterFaceVO dataInterFaceVO = new DataInterFaceVO();
            Long value = Double.valueOf(Math.random()*1000000000).longValue();
            if(value%3 == 0){
                dataInterFaceVO.setInterfaceType("rest");
                dataInterFaceVO.setAuthMethod(CoreConstant.AuthMethod.NOAUTH);
                dataInterFaceVO.setMicroServiceName("XGSIXTEEN");
            }else if(value%3 == 1){
                dataInterFaceVO.setInterfaceType("sql");
                dataInterFaceVO.setAuthMethod(CoreConstant.AuthMethod.ALLSYSTEMUSER);
                dataInterFaceVO.setMicroServiceName("General");
            }else if(value%3 == 2){
                dataInterFaceVO.setInterfaceType("sql");
                dataInterFaceVO.setAuthMethod(CoreConstant.AuthMethod.BYUSERPERMISSION);
                dataInterFaceVO.setMicroServiceName("HTCLOUD-PAYCENTER");
            }

            dataInterFaceVO.setInterfaceName("数据接口"+value);
            dataInterFaceVO.setUrlOrSqlContent("select * from table_"+i);
            dataInterFaceVO.addParam("col1",MongoEnumVO.DataType.String,"参数1");
            dataInterFaceVO.addParam("col2",MongoEnumVO.DataType.Long,"参数2");
            dataInterFaceVO.addParam("col6",MongoEnumVO.DataType.Double,"参数3");
            dataInterFaceVO.addParam("col6",MongoEnumVO.DataType.Date,"日期");
            dataInterFaceVO.addParam("col7",MongoEnumVO.DataType.Date,"日期");
            dataInterFaceVO.setCreatedOrUpdateTime(new Date());
            dataInterFaceVO.setCreatedOrUpdateBy(1L);
            dataInterFaceVO.setCreatedOrUpdateUserName("admin");
            dataInterFaceVOS.add(dataInterFaceVO);
        }

        mongoTemplate.insertAll(dataInterFaceVOS);

        Query query = new Query();

//        List<DataInterFaceVO> result = mongoTemplate.find(query.skip(0).limit(10),DataInterFaceVO.class);
//        BasicBSONObject index = new BasicBSONObject();
//        result = null;

//        List<MongoQueryParam> params = new ArrayList<>();
//
//        final MongoQueryParam andWhere = new MongoQueryParam();
//        andWhere.setName("interfaceName");
//        andWhere.setRelationalOperator(MongoEnumVO.RelationalOperator.AND);
//        andWhere.setOperator(MongoEnumVO.MongoOperatorEnum.REGEX);
//        andWhere.setValue("数据接口");
//
//        final MongoQueryParam andWhere1 = new MongoQueryParam();
//        andWhere1.setName("params.0.fieldName");
//        andWhere1.setRelationalOperator(MongoEnumVO.RelationalOperator.AND);
//        andWhere1.setOperator(MongoEnumVO.MongoOperatorEnum.EXISTS);
//        andWhere1.setValue(true);
//
//        final MongoQueryParam andWhere2 = new MongoQueryParam();
//        andWhere2.setName("params.2.fieldType");
//        andWhere2.setRelationalOperator(MongoEnumVO.RelationalOperator.AND);
//        andWhere2.setOperator(MongoEnumVO.MongoOperatorEnum.IS);
//        andWhere2.setValue("Number");
//
//        final MongoQueryParam orWhere = new MongoQueryParam();
//        orWhere.setName("_id");
//        orWhere.setRelationalOperator(MongoEnumVO.RelationalOperator.OR);
//        orWhere.setOperator(MongoEnumVO.MongoOperatorEnum.NIN);
//        orWhere.setValue(new ObjectId("5e65d88b75edf234484c1164"));
//
//        final MongoQueryParam norWhere = new MongoQueryParam();
//
//        norWhere.setName("_id");
//        norWhere.setRelationalOperator(MongoEnumVO.RelationalOperator.NOR);
//        norWhere.setOperator(MongoEnumVO.MongoOperatorEnum.IS);
//        norWhere.setValue(new ObjectId("5e65d88b75edf234484c1164"));
//
//
//        params.add(andWhere);
//        params.add(andWhere1);
//        params.add(andWhere1);
//        params.add(andWhere2);
//
//        List<Sort.Order> orders = new ArrayList<>();
//        orders.add(new Sort.Order(Sort.Direction.DESC,"interfaceName"));
////        params.add(orWhere);
////        params.add(norWhere);
//
//        Query query = MongoDBUtil.single().buildQuery(params);
//
//        // 排序
//        query = query.with(Sort.by(orders));
//
//
//        List<DataInterFaceVO> result = mongoTemplate.find(query,DataInterFaceVO.class);
//
//        result = null;
    }

}