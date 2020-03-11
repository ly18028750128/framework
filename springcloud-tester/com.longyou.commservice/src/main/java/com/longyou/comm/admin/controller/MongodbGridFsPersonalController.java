package com.longyou.comm.admin.controller;

import com.alibaba.fastjson.util.IOUtils;
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
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping(value = "/personal/mongo/gridfs", produces = MediaType.APPLICATION_JSON_VALUE)
@SystemResource(path = "/personal/mongo")
public class MongodbGridFsPersonalController {

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    MongoTemplate mongoTemplate;

    @SystemResource(value = "查询个人上传文件", description = "查询个人上传文件，需登录", authMethod = CoreConstant.AuthMethod.ALLSYSTEMUSER)
    @PostMapping("/list/{page}/{pageSize}")
    public ResponseResult list(@PathVariable("page") int page, @PathVariable("pageSize") int pageSize, @RequestBody Map<String, Object> params) throws Exception {
        ResponseResult result = ResponseResult.createSuccessResult();
        params.put(MongoDBEnum.metadataOwnerKey.value(), RequestContextManager.single().getRequestContext().getUser().getId());
        result.setData(MongoDBUtil.single().listFilePage(page, pageSize, params));
        return result;
    }

    @PostMapping("/upload")
    @SystemResource(value = "上传文件", description = "上传文件，需要登录", authMethod = CoreConstant.AuthMethod.ALLSYSTEMUSER)
    public ResponseResult upload(@RequestParam("file") MultipartFile file, @RequestParam Map<String, Object> params) throws Exception {
        final BasicDBObject doc = new BasicDBObject();
        doc.append(MongoDBEnum.metadataContentTypeKey.value(), file.getContentType());
        LoginUserDetails user = RequestContextManager.single().getRequestContext().getUser();
        if (user != null) {
            doc.append(MongoDBEnum.metadataOwnerKey.value(), user.getId());
            doc.append(MongoDBEnum.metadataOwnerNameKey.value(), user.getUsername());
            doc.append(MongoDBEnum.metadataOwnerFullNameKey.value(), user.getFullName());
        } else {
            doc.append(MongoDBEnum.metadataOwnerKey.value(), MongoDBEnum.defaultFileOwnerId.getLong());  //游客
        }
        final int suffixIndex = file.getOriginalFilename().lastIndexOf(".");
        if (suffixIndex > -1) {
            doc.append(MongoDBEnum.metadataFilesSuffixFieldName.value(), file.getOriginalFilename().substring(suffixIndex));
        }
        if (params != null) {
            params.forEach((key, value) -> {
                if (!(key.equals(MongoDBEnum.metadataOwnerKey.value()) || key.equals(MongoDBEnum.metadataOwnerNameKey.value()) ||
                        key.equals(MongoDBEnum.metadataOwnerFullNameKey.value()) || key.equals(MongoDBEnum.metadataContentTypeKey.value()))
                ) { // 系统内置的参数不能通过参数传递进行传入
                    doc.append(key, value);
                }
            });
        }
        ResponseResult result = ResponseResult.createSuccessResult();
        result.setData(gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), doc).toString());
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
    @SystemResource(value = "删除文件", description = "删除文件，仅登录用户删除自己的文件", authMethod = CoreConstant.AuthMethod.ALLSYSTEMUSER)
    public ResponseResult delete(@RequestBody List<String> ids) throws Exception {
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
    public void showFile(@RequestParam("_id") String _id, ServletResponse response) throws Exception {
        GridFSFile gridFSFile = MongoDBUtil.single().getGridFSFileByObjectId(_id);
        if (gridFSFile.getMetadata().get(MongoDBEnum.metadataOwnerKey.value()).equals(RequestContextManager.single().getRequestContext().getUser().getId())) {
            MongoDBUtil.single().downloadOrShowFile(gridFSFile, response, false);
        } else {
            throw new BusinessException("您没有权限查看此文件", null, HttpStatus.UNAUTHORIZED.value());
        }
    }
}
