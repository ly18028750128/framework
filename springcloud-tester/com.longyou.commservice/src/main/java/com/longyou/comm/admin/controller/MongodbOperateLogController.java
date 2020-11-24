package com.longyou.comm.admin.controller;

import com.github.pagehelper.PageInfo;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/admin/mongo/OperateLog", produces = MediaType.APPLICATION_JSON_VALUE)
@SystemResource(path = "/admin/mongo/OperateLog")
public class MongodbOperateLogController {
    @Autowired
    MongoTemplate mongoTemplate;
    @Value("${spring.application.group:}")
    String appGroup;

    @SystemResource(value = "获取操作日志", description = "管理员后台查询日志，需要授权", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @PostMapping("/list/{page}/{pageSize}")
    public ResponseResult listPage(@PathVariable("page") int page, @PathVariable("pageSize") int pageSize, @RequestBody Map<String, Object> queryParams) throws Exception {
        ResponseResult result = ResponseResult.createSuccessResult();
        if (StringUtils.isEmpty(queryParams.get("microName"))){
            return result;
        }
        String collectionName = appGroup + queryParams.get("microName") + CoreConstant.MongoDbLogConfig.MONGODB_OPERATE_LOG_SUFFIX.value();
        queryParams.remove("microName");
        Query query = new Query();
        //遍历查询条件并添加条件
        queryParams.forEach((key, value) -> {
            if (key.equals("startTime")){
                query.addCriteria(Criteria.where(key).gte(value));
            }else if (key.equals("endTime")){

            }else {
                query.addCriteria(Criteria.where(key).lt(value));
            }
        });
        //分页查询数据
        List<Map> listData = mongoTemplate.find(query.skip((page - 1) * pageSize).limit(pageSize), Map.class, collectionName);
        PageInfo pageInfo = new PageInfo(listData);
        pageInfo.setPageNum(page);
        pageInfo.setPageSize(pageSize);
        //查询总页数
        pageInfo.setTotal(mongoTemplate.getCollection(collectionName).countDocuments(query.getQueryObject()));
        result.setData(pageInfo);
        return result;
    }
}
