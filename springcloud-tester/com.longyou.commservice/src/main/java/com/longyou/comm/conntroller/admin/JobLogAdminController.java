package com.longyou.comm.conntroller.admin;

import static com.longyou.comm.CommonMenuConst.MENU_JOB_LOG_MANAGER;
import static com.longyou.comm.CommonMenuConst.MENU_SYSTEM_MANAGER;
import static org.cloud.scheduler.interceptor.JobTaskLogCustomizer.JOB_LOGS_COLLECTION;

import com.github.pagehelper.PageInfo;
import com.longyou.comm.dto.JobLogQueryDTO;
import com.unknow.first.mongo.dto.MongoQueryParamsDTO;
import com.unknow.first.mongo.param.MongoPagedParam;
import com.unknow.first.mongo.utils.MongoDBUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.cloud.dimension.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.scheduler.dto.JobTaskLog;
import org.cloud.vo.CommonApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api(tags = "管理员:定时任务日志管理", value = "管理员:定时任务日志管理")
@RequestMapping("/admin/job/logs")
@SystemResource(path = "/admin/job/logs", parentMenuCode = MENU_SYSTEM_MANAGER, parentMenuName = "系统管理")
@Slf4j
public class JobLogAdminController {
    @SystemResource(value = "/list", description = "管理员查询消息日志", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION, menuName = "定时任务日志", menuCode = MENU_JOB_LOG_MANAGER)
    @GetMapping
    @ApiOperation("管理员查询定时任务日志")
    public CommonApiResult<PageInfo<JobTaskLog>> list(JobLogQueryDTO jobLogQueryDTO, MongoPagedParam mongoPagedParam) throws Exception {
        MongoQueryParamsDTO mongoQueryParamsDTO = MongoDBUtil.single().buildQueryParamsDTO(jobLogQueryDTO, mongoPagedParam);
        PageInfo<JobTaskLog> result = MongoDBUtil.single()
            .paged(mongoPagedParam.getPage().longValue(), mongoPagedParam.getLimit().longValue(), mongoQueryParamsDTO, JobTaskLog.class, JOB_LOGS_COLLECTION);
        return CommonApiResult.createSuccessResult(result);
    }
}
