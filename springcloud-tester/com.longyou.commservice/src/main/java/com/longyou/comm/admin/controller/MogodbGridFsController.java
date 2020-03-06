package com.longyou.comm.admin.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.github.pagehelper.PageInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.cloud.constant.CoreConstant;
import org.cloud.vo.MongoDbGridFsVO;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

@RestController
@RequestMapping(value = "/admin/mongo/gridfs", produces = MediaType.APPLICATION_JSON_VALUE)
public class MogodbGridFsController {

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    MongoTemplate mongoTemplate;

    @PostMapping("/upload")
    public ResponseResult upload(@RequestParam("file") MultipartFile file, @RequestParam Map<String, Object> params) throws Exception {
        final BasicDBObject doc = new BasicDBObject();
        doc.append("originalFilename", file.getOriginalFilename());
        gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), doc);
        return ResponseResult.createSuccessResult();
    }

    @PostMapping("/list/{page}/{pageSize}")
    public ResponseResult list(@PathVariable("page") int page, @PathVariable("pageSize") int pageSize, @RequestBody Map<String, Object> params) throws Exception {
        Query query = new Query();
        if (!StringUtils.isEmpty(params.get("filename"))) {
            query.addCriteria(Criteria.where("filename").regex("^.*" + params.get("filename") + ".*$"));
        }
        Criteria lengthWhere = null;
        if (!StringUtils.isEmpty(params.get("minSize"))) {
            lengthWhere = Criteria.where("length");
            lengthWhere.gte(params.get("minSize"));
        }
        if (!StringUtils.isEmpty(params.get("maxSize"))) {
            if (lengthWhere == null) {
                lengthWhere = Criteria.where("length");
            }
            lengthWhere.lte(params.get("maxSize"));
        }
        if (lengthWhere != null) {
            query.addCriteria(lengthWhere);
        }

        if (params.get("uploadDate") != null) {
            List<String> dateRange = (List<String>) params.get("uploadDate");
            query.addCriteria(Criteria.where("uploadDate")
                    .gte(CoreConstant.DateTimeFormat.ISODATE.getDateFormat().parse(dateRange.get(0)))
                    .lte(CoreConstant.DateTimeFormat.ISODATE.getDateFormat().parse(dateRange.get(1))));
        }
        if (!StringUtils.isEmpty(params.get("_id"))) {
            query.addCriteria(Criteria.where("_id").is(new ObjectId(params.get("_id").toString())));
        }
        query = query.skip((page - 1) * pageSize).limit(pageSize);
        List<MongoDbGridFsVO> listData = mongoTemplate.find(query, MongoDbGridFsVO.class, "fs.files");
        ResponseResult result = ResponseResult.createSuccessResult();
        PageInfo pageInfo = new PageInfo(listData);
        pageInfo.setPageNum(page);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotal(mongoTemplate.getCollection("fs.files").countDocuments());
        result.setData(pageInfo);
        return result;
    }


}
