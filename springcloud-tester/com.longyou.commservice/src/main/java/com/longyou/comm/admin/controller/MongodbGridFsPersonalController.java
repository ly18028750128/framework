package com.longyou.comm.admin.controller;

import com.github.pagehelper.PageInfo;
import com.mongodb.client.gridfs.model.GridFSFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.servlet.ServletResponse;
import org.bson.types.ObjectId;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;
import org.cloud.exception.BusinessException;
import org.cloud.utils.mongo.MetadataDTO;
import org.cloud.utils.mongo.MongoDBEnum;
import org.cloud.utils.mongo.MongoDBUtil;
import org.cloud.utils.mongo.MongoGridFsQueryDTO;
import org.cloud.vo.CommonApiResult;
import org.cloud.vo.MongoDbGridFsVO;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/personal/mongo/gridfs")
@SystemResource(path = "/personal/mongo")
@Api(description = "mongodb：个人文件管理", tags = "mongodb：个人文件管理")
public class MongodbGridFsPersonalController {

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    MongoTemplate mongoTemplate;

    @SystemResource(value = "查询个人上传文件", description = "查询个人上传文件，需登录", authMethod = CoreConstant.AuthMethod.ALLSYSTEMUSER)
    @PostMapping("/list/{page}/{pageSize}")
    @ApiOperation(value = "查询个人文件列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "page", value = "页码", dataType = "int", required = true, paramType = "query", example = "1"),
        @ApiImplicitParam(name = "pageSize", value = "每页行数", dataType = "int", required = true, paramType = "query", example = "10"),
        @ApiImplicitParam(name = "params", value = "查询参数(filename/uploadDate/minSize/maxSize/_id)", dataType = "json", required = true, example = "{}")})
    public CommonApiResult<PageInfo<MongoDbGridFsVO>> list(@PathVariable("page") int page, @PathVariable("pageSize") int pageSize,
        @RequestBody MongoGridFsQueryDTO params) throws Exception {
        params.getMetadata().setOwner(RequestContextManager.single().getRequestContext().getUser().getId());
        return CommonApiResult.createSuccessResult(MongoDBUtil.single().listFilePage(page, pageSize, params));
    }

    @PostMapping(value = "/upload")
    @SystemResource(value = "上传文件", description = "上传文件，需要登录", authMethod = CoreConstant.AuthMethod.ALLSYSTEMUSER)
    @ApiOperation(value = "文件上传")
    @ApiImplicitParams({})
    public CommonApiResult<Serializable> upload(@RequestPart("file") MultipartFile file, MetadataDTO params) throws Exception {
        LoginUserDetails user = RequestContextManager.single().getRequestContext().getUser();
        if (user != null) {
            params.setOwner(user.getId());
            params.setOwnerName(user.getUsername());
            params.setOwnerFullName(user.getFullName());
        } else {
            params.setOwner(MongoDBEnum.defaultFileOwnerId.getLong());
        }
        final int suffixIndex = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".");
        if (suffixIndex > -1) {
            params.setSuffix(file.getOriginalFilename().substring(suffixIndex));
        }
        String contentType = file.getContentType() == null ? "unknown" : file.getContentType();
        params.setContentType(contentType);
        return CommonApiResult.createSuccessResult(gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), contentType, params).toString());

//        return CommonApiResult.createSuccessResult("true");
    }

    /**
     * 只能按id删除
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @PostMapping("/delete")
    @SystemResource(value = "删除文件", description = "删除文件，仅登录用户删除自己的文件", authMethod = CoreConstant.AuthMethod.ALLSYSTEMUSER)
    @ApiOperation(value = "删除个人文件")
    @ApiImplicitParams({@ApiImplicitParam(name = "ids", value = "文件_id列表", dataType = "list", required = true, paramType = "body", example = "['_id1']")})
    public ResponseResult<?> delete(@RequestBody List<String> ids) throws Exception {
        Query query = new Query();
        List<ObjectId> objectIds = ids.stream().map(ObjectId::new).collect(Collectors.toList());  //转换成新的对对象
        query.addCriteria(Criteria.where("_id").in(objectIds));
        query.addCriteria(Criteria.where(MongoDBEnum.metadataKey.value() + "." + MongoDBEnum.metadataOwnerKey.value())
            .in(RequestContextManager.single().getRequestContext().getUser().getId()));  //
        gridFsTemplate.delete(query);
        return ResponseResult.createSuccessResult();
    }

    /**
     * 下载文件
     *
     * @param _id
     * @return
     * @throws Exception
     */
    @GetMapping("/download")
    @SystemResource(value = "下载本人文件", description = "下载文件，仅可下载本人的文件", authMethod = CoreConstant.AuthMethod.ALLSYSTEMUSER)
    @ApiOperation(value = "下载个人文件")
    @ApiImplicitParams({@ApiImplicitParam(name = "_id", value = "文件_id", dataType = "string", required = true, paramType = "query", example = "_id1")})
    public void download(@RequestParam("_id") String _id, ServletResponse response) throws Exception {
        GridFSFile gridFSFile = MongoDBUtil.single().getGridFSFileByObjectId(_id);
        if (gridFSFile.getMetadata().get(MongoDBEnum.metadataOwnerKey.value()).equals(RequestContextManager.single().getRequestContext().getUser().getId())) {
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
    @SystemResource(value = "查看本人文件", description = "查看本人文件，仅可查看本人的文件", authMethod = CoreConstant.AuthMethod.ALLSYSTEMUSER)
    @ApiOperation(value = "显示个人文件")
    @ApiImplicitParams({@ApiImplicitParam(name = "_id", value = "文件_id", dataType = "string", required = true, paramType = "query", example = "_id1")})
    public void showFile(@RequestParam("_id") String _id, ServletResponse response) throws Exception {
        GridFSFile gridFSFile = MongoDBUtil.single().getGridFSFileByObjectId(_id);
        assert gridFSFile.getMetadata() != null;
        if (gridFSFile.getMetadata().get(MongoDBEnum.metadataOwnerKey.value()).equals(RequestContextManager.single().getRequestContext().getUser().getId())) {
            MongoDBUtil.single().downloadOrShowFile(gridFSFile, response, false);
        } else {
            throw new BusinessException("您没有权限查看此文件", null, HttpStatus.UNAUTHORIZED.value());
        }
    }
}
