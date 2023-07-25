package com.longyou.comm.admin.service;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.unknow.first.mongo.vo.DataInterFaceVO;

import java.util.List;


public interface IDataInterFaceService {

    public List<Object> saveOrUpdateByIds(final List<DataInterFaceVO> dataInterFaceVOS) throws Exception;

    public UpdateResult inactiveByIds(final List<String> ids) throws Exception;  //失效

    public UpdateResult activeByIds(final List<String> ids) throws Exception;  //失效

    public DeleteResult deleteByIds(final List<String> ids) throws Exception;  //失效

}
