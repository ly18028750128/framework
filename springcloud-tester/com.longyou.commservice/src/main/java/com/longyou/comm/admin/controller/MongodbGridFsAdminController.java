package com.longyou.comm.admin.controller;

import com.mongodb.client.gridfs.model.GridFSFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.ServletResponse;
import org.bson.types.ObjectId;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.utils.mongo.MongoDBUtil;
import org.cloud.vo.MongoDbGridFsVO;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin/mongo/gridfs", produces = MediaType.APPLICATION_JSON_VALUE)
@SystemResource(path = "/admin/mongo")
@Api(description = "mongodb：管理员后台", tags = "mongodb：管理员后台")
public class MongodbGridFsAdminController {

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    MongoTemplate mongoTemplate;

    @SystemResource(value = "查询上传文件", description = "管理员后台查询文件，需要授", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @PostMapping("/list/{page}/{pageSize}")
    @ApiOperation(value = "查询文件列表")
    @ApiImplicitParams(
        {
            @ApiImplicitParam(name = "page", value = "页码", dataType = "int", required = true, paramType = "query",example = "0"),
            @ApiImplicitParam(name = "pageSize", value = "每页行数", dataType = "int", required = true, paramType = "query" ,example = "0"),
            @ApiImplicitParam(name = "params", value = "查询参数(filename/uploadDate/minSize/maxSize)",dataType = "json", required = true ,example = "{}")
        }
    )
    public ResponseResult<MongoDbGridFsVO> list(@PathVariable("page") int page, @PathVariable("pageSize") int pageSize, @RequestBody Map<String, Object> params)
        throws Exception {
        ResponseResult<MongoDbGridFsVO> result = ResponseResult.createSuccessResult();
        result.setData(MongoDBUtil.single().listFilePage(page, pageSize, params));
        return result;
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
    public ResponseResult<?> delete(@RequestBody List<String> ids) throws Exception {
        Query query = new Query();
        List<ObjectId> objectIds = ids.stream().map(ObjectId::new).collect(Collectors.toList());  //转换成新的对对象
        query.addCriteria(Criteria.where("_id").in(objectIds));
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
    @ApiOperation(value = "查看文件")
    public void showFile(@RequestParam("_id") String _id, ServletResponse response) throws Exception {
        GridFSFile gridFSFile = MongoDBUtil.single().getGridFSFileByObjectId(_id);
        MongoDBUtil.single().downloadOrShowFile(gridFSFile, response, false);
    }

}
