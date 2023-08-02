package com.longyou.comm.admin.controller;

import com.github.pagehelper.PageInfo;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.unknow.first.mongo.dto.MongoGridFsQueryDTO;
import com.unknow.first.mongo.utils.MongoDBUtil;
import com.unknow.first.mongo.vo.MongoDbGridFsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletResponse;
import org.bson.types.ObjectId;
import org.cloud.constant.CoreConstant;
import org.cloud.dimension.annotation.SystemResource;
import org.cloud.vo.CommonApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin/mongo/gridfs")
@SystemResource(path = "/admin/mongo")
@Api(description = "后台管理-文章管理", tags = "mongodb：管理员后台")
public class MongodbGridFsAdminController {

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    MongoTemplate mongoTemplate;

    @SystemResource(value = "查询上传文件", description = "管理员后台查询文件，需要授", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @PostMapping(value = "/list/{page}/{pageSize}")
    @ApiOperation(value = "查询文件列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "page", value = "页码", dataType = "int", required = true, paramType = "query", example = "0"),
        @ApiImplicitParam(name = "pageSize", value = "每页行数", dataType = "int", required = true, paramType = "query", example = "0"),
        @ApiImplicitParam(name = "params", value = "查询参数(filename/uploadDate/minSize/maxSize)", dataType = "json", required = true, example = "{}")})
    public CommonApiResult<PageInfo<MongoDbGridFsVO>> list(@PathVariable("page") int page, @PathVariable("pageSize") int pageSize,
        @RequestBody MongoGridFsQueryDTO params) throws Exception {
        return CommonApiResult.createSuccessResult(MongoDBUtil.single().listFilePage(page, pageSize, params));
    }

    /**
     * 只能按id删除
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @PostMapping("/delete")
    @SystemResource(value = "删除文件", description = "管理员后台删除文件，需要授", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @ApiOperation(value = "删除文件")
    public CommonApiResult<?> delete(@RequestBody List<String> ids) throws Exception {
        Query query = new Query();
        List<ObjectId> objectIds = ids.stream().map(ObjectId::new).collect(Collectors.toList());  //转换成新的对对象
        query.addCriteria(Criteria.where("_id").in(objectIds));
        gridFsTemplate.delete(query);
        return CommonApiResult.createSuccessResult();
    }

    /**
     * 下载文件
     *
     * @param _id
     * @return
     * @throws Exception
     */
    @GetMapping("/download")
    @SystemResource(value = "下载文件", description = "管理员后台下载文件，需要授", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @ApiOperation(value = "下载文件")
    public void download(@RequestParam("_id") String _id, ServletResponse response) throws Exception {
        GridFSFile gridFSFile = MongoDBUtil.single().getGridFSFileByObjectId(_id);
        MongoDBUtil.single().downloadOrShowFile(gridFSFile, response, true);
    }

    /**
     * 直接展示文件
     *
     * @param _id
     * @param response
     * @throws Exception
     */
    @GetMapping("/showFile")
    @SystemResource(value = "查看文件", description = "管理员后台查看文件，需要授", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @ApiOperation(value = "下载文件")
    public void showFile(@RequestParam("_id") String _id, ServletResponse response) throws Exception {
        GridFSFile gridFSFile = MongoDBUtil.single().getGridFSFileByObjectId(_id);
        MongoDBUtil.single().downloadOrShowFile(gridFSFile, response, false);
    }

}
