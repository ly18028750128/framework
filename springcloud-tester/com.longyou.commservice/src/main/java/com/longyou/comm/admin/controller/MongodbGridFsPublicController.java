package com.longyou.comm.admin.controller;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.cloud.exception.BusinessException;
import org.cloud.utils.mongo.MongoDBEnum;
import org.cloud.utils.mongo.MongoDBUtil;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 公共文件查询
 */
@RestController
@RequestMapping(value = "/public/mongo/gridfs", produces = MediaType.APPLICATION_JSON_VALUE)
public class MongodbGridFsPublicController {

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    MongoTemplate mongoTemplate;


    @PostMapping("/list/{page}/{pageSize}")
    public ResponseResult list(@PathVariable("page") int page, @PathVariable("pageSize") int pageSize,
                               @RequestBody Map<String, Object> params) throws Exception {
        ResponseResult result = ResponseResult.createSuccessResult();
        final Map<String, Object> metadataQueryParams = new LinkedHashMap<>(); // metadata查询参数
        metadataQueryParams.put(MongoDBEnum.metadataFileAuthRangeFieldName.value(), MongoDBEnum.metadataFileAuthRangePublic.value());
        params.put(MongoDBEnum.metadataKey.value(), metadataQueryParams);
        result.setData(MongoDBUtil.single().listFilePage(page, pageSize, params));
        return result;
    }

    /**
     * 下载文件
     *
     * @param _id
     * @return
     * @throws Exception
     */
    @GetMapping("/download")
    public void download(@RequestParam("_id") String _id, ServletResponse response) throws Exception {
        GridFSFile gridFSFile = MongoDBUtil.single().getGridFSFileByObjectId(_id);
        if (gridFSFile.getMetadata().get(MongoDBEnum.metadataFileAuthRangeFieldName.value()).equals(MongoDBEnum.metadataFileAuthRangePublic.value())) {
            MongoDBUtil.single().downloadOrShowFile(gridFSFile, response, true);
        } else {
            throw new BusinessException("您没有权限下载此文件", null, HttpStatus.UNAUTHORIZED.value());
        }
    }

    /**
     * 直接展示文件
     *
     * @param _id
     * @param response
     * @throws Exception
     */
    @GetMapping("/showFile")
    public void showFile(@RequestParam("_id") String _id, ServletResponse response) throws Exception {
        GridFSFile gridFSFile = MongoDBUtil.single().getGridFSFileByObjectId(_id);
        if (gridFSFile.getMetadata().get(MongoDBEnum.metadataFileAuthRangeFieldName.value()).equals(MongoDBEnum.metadataFileAuthRangePublic.value())
                || gridFSFile.getMetadata().get(MongoDBEnum.metadataFileAuthRangeFieldName.value()).equals(MongoDBEnum.metadataFileAuthRangeResource.value())
        ) {
            MongoDBUtil.single().downloadOrShowFile(gridFSFile, response, false);
        } else {
            throw new BusinessException("您没有权限查看此文件", null, HttpStatus.UNAUTHORIZED.value());
        }
    }

}
