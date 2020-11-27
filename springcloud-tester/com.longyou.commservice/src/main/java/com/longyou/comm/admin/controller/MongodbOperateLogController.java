package com.longyou.comm.admin.controller;

import com.github.pagehelper.PageInfo;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
        Criteria criteria = new Criteria();
        if (!StringUtils.isEmpty(queryParams.get("userName"))){
            Pattern pattern= Pattern.compile("^.*"+ queryParams.get("userName") +".*$");
            criteria.and("userName").regex(pattern);
        }
        if (!StringUtils.isEmpty(queryParams.get("type"))){
            criteria.and("type").is(Integer.parseInt(String.valueOf(queryParams.get("type"))));
        }
        if (!StringUtils.isEmpty(queryParams.get("desc"))){
            Pattern pattern= Pattern.compile("^.*"+ queryParams.get("desc") +".*$");

            criteria.orOperator(Criteria.where("resMsg").regex(pattern),
                    Criteria.where("uri").regex(pattern),
                    Criteria.where("reqIp").regex(pattern),
                    Criteria.where("params").regex(pattern),
                    Criteria.where("desc").regex(pattern));
        }
        if (!StringUtils.isEmpty(queryParams.get("startTime"))){
            criteria.and("createDate").gte(strToDateLong(queryParams.get("startTime").toString()));
        }
        if (!StringUtils.isEmpty(queryParams.get("endTime"))){
            criteria.and("createDate").lt(strToDateLong(queryParams.get("endTime").toString()));
        }
        Query query = new Query(criteria);
        //排序
        Sort.Order createTimeDesc = new Sort.Order(Sort.Direction.DESC, "createDate");
        ArrayList<Sort.Order> orders = new ArrayList<>();
        orders.add(createTimeDesc);
        query.with(Sort.by(orders));
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

    public static Date strToDateLong(String strDate) {
        Date strtodate = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            strtodate = formatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strtodate;
    }
}
