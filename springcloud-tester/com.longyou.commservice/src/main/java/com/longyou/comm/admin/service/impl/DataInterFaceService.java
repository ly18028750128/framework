package com.longyou.comm.admin.service.impl;

import com.longyou.comm.admin.service.IDataInterFaceService;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.unknow.first.mongo.utils.MongoDBUtil;
import org.bson.types.ObjectId;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;
import com.unknow.first.mongo.vo.DataInterFaceVO;
import org.cloud.utils.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("mongoDataInterFaceService")
public class DataInterFaceService implements IDataInterFaceService {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<DataInterFaceVO> saveOrUpdateByIds(List<DataInterFaceVO> dataInterFaceVOS) throws Exception {
        final List<DataInterFaceVO> result = new ArrayList<>();
        LoginUserDetails user = RequestContextManager.single().getRequestContext().getUser();
        for (DataInterFaceVO interFaceVO : dataInterFaceVOS) {
            interFaceVO.setCreatedOrUpdateBy(user.getId());
            interFaceVO.setCreatedOrUpdateUserName(user.getUsername());
            interFaceVO.setCreatedOrUpdateTime(new Date());
            if (!StringUtils.hasLength(interFaceVO.get_id())) {
                interFaceVO.setMd5(MD5Encoder.encode(interFaceVO.getInterfaceName()));
                result.add(mongoTemplate.insert(interFaceVO));
            } else {
                Query query = new Query();
                query.addCriteria(Criteria.where("_id").is(new ObjectId(interFaceVO.get_id())));
                Update update = MongoDBUtil.single().buildUpdateByObject(interFaceVO);
                mongoTemplate.updateFirst(query, update, DataInterFaceVO.class);
            }
        }
        return result;

    }

    @Override
    public UpdateResult inactiveByIds(List<String> ids) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(MongoDBUtil.single().convertIdsToObjectId(ids)));
        Update update = Update.update("status", 0);
        return mongoTemplate.updateMulti(query, update, DataInterFaceVO.class);
    }

    @Override
    public UpdateResult activeByIds(List<String> ids) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(MongoDBUtil.single().convertIdsToObjectId(ids)));
        Update update = Update.update("status", 1);
        return mongoTemplate.updateMulti(query, update, DataInterFaceVO.class);
    }

    @Override
    public DeleteResult deleteByIds(List<String> ids) throws Exception {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").in(MongoDBUtil.single().convertIdsToObjectId(ids)));
        return mongoTemplate.remove(query, DataInterFaceVO.class);
    }


}
