package com.longyou.comm.admin.controller;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;
import org.cloud.exception.BusinessException;
import org.cloud.utils.mongo.MongoDBEnum;
import org.cloud.utils.mongo.MongoDBUtil;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/file/mongo", produces = MediaType.APPLICATION_JSON_VALUE)
@SystemResource(path = "/resource/mongo", description = "系统资源文件管理，登录用户可以上传，任何人都可以查看，仅可上传图片，text，html文件等")
public class MongodbFileShowController {

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    MongoTemplate mongoTemplate;

    /**
     * 显示公共文件和资源文件
     *
     * @param _id
     * @return
     * @throws Exception
     */
    @GetMapping("show/{objectId}")
    public void show(@PathVariable("objectId") String _id, ServletResponse response) throws Exception {
        GridFSFile gridFSFile = MongoDBUtil.single().getGridFSFileByObjectId(_id);
        if (MongoDBUtil.single().isPersonalFile(gridFSFile)) {
            throw new BusinessException("个人文件不能查看!", gridFSFile.getFilename());
        }
        MongoDBUtil.single().downloadOrShowFile(gridFSFile, response, false);
    }

    /**
     * 下载公共文件和资源文件
     *
     * @param _id
     * @return
     * @throws Exception
     */
    @GetMapping("download/{objectId}")
    public void download(@PathVariable("objectId") String _id, ServletResponse response) throws Exception {
        GridFSFile gridFSFile = MongoDBUtil.single().getGridFSFileByObjectId(_id);
        if (MongoDBUtil.single().isPersonalFile(gridFSFile)) {
            throw new BusinessException("个人文件不能下载!", gridFSFile.getFilename());
        }
        MongoDBUtil.single().downloadOrShowFile(gridFSFile, response, true);
    }
}
