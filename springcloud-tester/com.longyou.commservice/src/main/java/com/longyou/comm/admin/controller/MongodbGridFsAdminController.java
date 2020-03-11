package com.longyou.comm.admin.controller;

import com.alibaba.fastjson.util.IOUtils;
import com.github.pagehelper.PageInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.context.RequestContextManager;
import org.cloud.utils.mongo.MongoDBEnum;
import org.cloud.utils.mongo.MongoDBUtil;
import org.cloud.vo.MongoDbGridFsVO;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/admin/mongo/gridfs", produces = MediaType.APPLICATION_JSON_VALUE)
@SystemResource(path="/admin/mongo")
public class MongodbGridFsAdminController {

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    MongoTemplate mongoTemplate;

    @SystemResource(value = "查询上传文件",description = "管理员后台查询文件，需要授",authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @PostMapping("/list/{page}/{pageSize}")
    public ResponseResult list(@PathVariable("page") int page, @PathVariable("pageSize") int pageSize, @RequestBody Map<String, Object> params) throws Exception {
        ResponseResult result = ResponseResult.createSuccessResult();
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
    @SystemResource(value = "删除文件",description = "管理员后台删除文件，需要授",authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    public ResponseResult delete(@RequestBody List<String> ids) throws Exception {
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
    @SystemResource(value = "下载文件",description = "管理员后台下载文件，需要授",authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
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
    @SystemResource(value = "查看文件",description = "管理员后台查看文件，需要授",authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    public void showFile(@RequestParam("_id") String _id, ServletResponse response) throws Exception {
        GridFSFile gridFSFile = MongoDBUtil.single().getGridFSFileByObjectId(_id);
        MongoDBUtil.single().downloadOrShowFile(gridFSFile, response, false);
    }

}
