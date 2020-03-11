package com.longyou.comm.admin.service;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.cloud.mongo.DataInterFaceVO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface IDataInterFaceService {

    public List<Object> saveOrUpdateByIds(final List<DataInterFaceVO> dataInterFaceVOS) throws Exception;

    public UpdateResult inactiveByIds(final List<String> ids) throws Exception;  //失效

    public UpdateResult activeByIds(final List<String> ids) throws Exception;  //失效

    public DeleteResult deleteByIds(final List<String> ids) throws Exception;  //失效

}
