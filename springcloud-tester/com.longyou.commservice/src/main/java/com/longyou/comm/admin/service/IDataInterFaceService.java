package com.longyou.comm.admin.service;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.unknow.first.mongo.vo.DataInterFaceVO;
import java.util.List;


public interface IDataInterFaceService {

    List<DataInterFaceVO> saveOrUpdateByIds(final List<DataInterFaceVO> dataInterFaceVOS) throws Exception;

    UpdateResult inactiveByIds(final List<String> ids) throws Exception;  //失效

    UpdateResult activeByIds(final List<String> ids) throws Exception;  //失效

    DeleteResult deleteByIds(final List<String> ids) throws Exception;  //失效

}
