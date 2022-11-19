package com.longyou.comm.admin.controller;

import static org.cloud.utils.mongo.MongoDBEnum.metadataFileAuthRangePublic;
import static org.cloud.utils.mongo.MongoDBEnum.metadataFileAuthRangeResource;

import com.github.pagehelper.PageInfo;
import com.mongodb.client.gridfs.model.GridFSFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import javax.servlet.ServletResponse;
import org.cloud.exception.BusinessException;
import org.cloud.utils.mongo.MongoDBEnum;
import org.cloud.utils.mongo.MongoDBUtil;
import org.cloud.utils.mongo.MongoGridFsQueryDTO;
import org.cloud.vo.CommonApiResult;
import org.cloud.vo.MongoDbGridFsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公共文件查询
 */
@RestController
@RequestMapping(value = "/public/mongo/gridfs", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(description = "mongodb：公共和资源文件查看或者下载", tags = "mongodb：公共和资源文件查看或者下载")
public class MongodbGridFsPublicController {

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    MongoTemplate mongoTemplate;


    @PostMapping("/list/{page}/{pageSize}")
    @ApiOperation(value = "查询公共(资源)文件列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "page", value = "页码", dataType = "int", required = true, paramType = "query", example = "0"),
        @ApiImplicitParam(name = "pageSize", value = "每页行数", dataType = "int", required = true, paramType = "query", example = "0"),
        @ApiImplicitParam(name = "params", value = "查询参数(filename/uploadDate/minSize/maxSize/_id)", dataType = "json", required = true, example = "{}")})
    public CommonApiResult<PageInfo<MongoDbGridFsVO>> list(@PathVariable("page") int page, @PathVariable("pageSize") int pageSize,
        @RequestBody MongoGridFsQueryDTO params) throws Exception {
        params.getMetadata().setFileAuthRangeList(Arrays.asList(metadataFileAuthRangePublic.value(), metadataFileAuthRangeResource.value()));
        return CommonApiResult.createSuccessResult(MongoDBUtil.single().listFilePage(page, pageSize, params));
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
        if (checkIsPublicOrResource(gridFSFile)) {
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
        if (checkIsPublicOrResource(gridFSFile)) {
            MongoDBUtil.single().downloadOrShowFile(gridFSFile, response, false);
        } else {
            throw new BusinessException("您没有权限查看此文件", null, HttpStatus.UNAUTHORIZED.value());
        }
    }

    private Boolean checkIsPublicOrResource(GridFSFile gridFSFile) {
        assert gridFSFile.getMetadata() != null;
        return Arrays.asList(metadataFileAuthRangePublic.value(), metadataFileAuthRangeResource.value())
            .contains(gridFSFile.getMetadata().get(MongoDBEnum.metadataFileAuthRangeFieldName.value()));
    }

}
