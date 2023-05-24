package com.unknow.first.imexport.controller;

import com.unknow.first.imexport.constant.ImexportConstants.TaskType;
import com.unknow.first.imexport.domain.FrameImportExportTask;
import com.unknow.first.imexport.feign.ImexportTaskFeignClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant.AuthMethod;
import org.cloud.utils.CommonUtil;
import org.cloud.vo.CommonApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.unknow.first.imexport.constant.ImexportMenuConstants.MENU_USER_EXCEL_PARENT;

@RestController
@RequestMapping("/remot/imexport/task")
@Api(description = "用户：导入导出任务管理", tags = "用户：导入导出任务管理")
@SystemResource(path = "/user/imexport/task", parentMenuCode = MENU_USER_EXCEL_PARENT, parentMenuName = "导入导出管理")
public class RemoteImexportUserController {

    @Autowired
    ImexportTaskFeignClient imexportTaskFeignClient;

    @ApiOperation(value = "创建任务(调用commonService)", notes = "创建任务(调用commonService)")
    @PostMapping(value = "")
    @SystemResource(value = "/create", description = "创建导入导出任务", authMethod = AuthMethod.BYUSERPERMISSION)
    public CommonApiResult<FrameImportExportTask> create(
        @ApiParam("任务名称") @RequestParam(value = "taskName") String taskName,
        @ApiParam("线程执行类的名称，导入继承ImportCallableService，导出继承ExportCallableService") @RequestParam(value = "processClass") String processClass,
        @ApiParam("模板编码，导出时有效，对应模板表里的编码") @RequestParam(value = "templateCode", required = false) String templateCode,
        @ApiParam("执行参数（JSON）") @RequestParam(value = "params", required = false) String params,
        @ApiParam(value = "文件类型", example = "xlsx") @RequestParam(value = "extension", defaultValue = "xlsx") String extension,
        @ApiParam(value = "任务类型") @RequestParam(value = "taskType") TaskType taskType,
        @ApiParam("需要导入的文件，上传时必传") @RequestPart(required = false, name = "file") MultipartFile file) throws Exception {
        if (TaskType.IMPORT.value == taskType.value) {
            Assert.notNull(file, "system.error.import.file.notEmpty");
        }
        String belongService = CommonUtil.single().getEnv("spring.application.name", "");
        return CommonApiResult.createSuccessResult(imexportTaskFeignClient.create(taskName, processClass, templateCode, params, extension, belongService, taskType, file));
    }
}
