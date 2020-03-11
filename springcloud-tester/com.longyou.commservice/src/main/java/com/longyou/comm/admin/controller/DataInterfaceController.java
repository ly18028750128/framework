package com.longyou.comm.admin.controller;

import com.longyou.comm.admin.service.IDataInterFaceService;
import org.cloud.mongo.DataInterFaceVO;
import org.cloud.mongo.MongoQueryParamsDTO;
import org.cloud.utils.mongo.MongoDBUtil;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据接口配置，基于mongodb
 */
@RestController("MongDbDataInterfaceController")
@RequestMapping("/admin/mongo/config/dataInterface")
public class DataInterfaceController {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    IDataInterFaceService dataInterFaceService;

    @PostMapping("/list/{page}/{pageSize}")
    public ResponseResult paged(@PathVariable("page") Long pageNum, @PathVariable("pageSize") Long pageSize, @RequestBody MongoQueryParamsDTO queryParamsDTO) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(MongoDBUtil.single().paged(pageNum, pageSize, queryParamsDTO, DataInterFaceVO.class));
        return responseResult;
    }

    @PostMapping("/saveOrUpdate")
    public ResponseResult updateByIds(@RequestBody List<DataInterFaceVO> dataInterFaceVOS) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(dataInterFaceService.saveOrUpdateByIds(dataInterFaceVOS));
        return responseResult;
    }

    @PostMapping("/inactiveByIds")
    public ResponseResult inactiveByIds(@RequestBody List<String> ObjectIds) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(dataInterFaceService.inactiveByIds(ObjectIds));
        return responseResult;
    }

    @PostMapping("/activeByIds")
    public ResponseResult activeByIds(@RequestBody List<String> ObjectIds) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(dataInterFaceService.activeByIds(ObjectIds));
        return responseResult;
    }

    @PostMapping("/deleteByIds")
    public ResponseResult deleteByIds(@RequestBody List<String> ObjectIds) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(dataInterFaceService.deleteByIds(ObjectIds));
        return responseResult;
    }

}
