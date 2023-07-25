package com.longyou.comm.admin.controller;

import com.mongodb.client.gridfs.model.GridFSFile;
import com.unknow.first.mongo.utils.MongoDBUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.cloud.dimension.annotation.SystemResource;
import org.cloud.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletResponse;

@RestController
@RequestMapping(value = "/file/mongo", produces = MediaType.APPLICATION_JSON_VALUE)
@SystemResource(path = "/resource/mongo", description = "系统资源文件管理，登录用户可以上传，任何人都可以查看，仅可上传图片，text，html文件等")
@Api(value = "mongo：显示下载非个人文件",tags = "mongo：显示下载非个人文件")
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
    @ApiOperation("查看文件")
    @ApiImplicitParams({@ApiImplicitParam(name = "objectId", value = "文件_id", dataType = "string", required = true, paramType = "path", example = "6378aaccca55bc6fff4f0eba")})
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
    @ApiOperation("下载文件")
    @ApiImplicitParams({@ApiImplicitParam(name = "objectId", value = "文件_id", dataType = "string", required = true, paramType = "path", example = "6378aaccca55bc6fff4f0eba")})
    public void download(@PathVariable("objectId") String _id, ServletResponse response) throws Exception {
        GridFSFile gridFSFile = MongoDBUtil.single().getGridFSFileByObjectId(_id);
        if (MongoDBUtil.single().isPersonalFile(gridFSFile)) {
            throw new BusinessException("个人文件不能下载!", gridFSFile.getFilename());
        }
        MongoDBUtil.single().downloadOrShowFile(gridFSFile, response, true);
    }
}
