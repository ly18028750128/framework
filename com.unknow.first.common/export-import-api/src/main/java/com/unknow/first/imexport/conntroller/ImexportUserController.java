package com.unknow.first.imexport.conntroller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.unknow.first.imexport.constant.ImexportConstants.TaskType;
import com.unknow.first.imexport.domain.FrameImportExportTask;
import com.unknow.first.imexport.dto.FrameImportExportTaskQueryDTO;
import com.unknow.first.imexport.dto.ImportExportTaskCreateDTO;
import com.unknow.first.imexport.service.FrameImportExportTaskService;
import com.unknow.first.mongo.utils.MongoDBUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.bson.types.ObjectId;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant.AuthMethod;
import org.cloud.constant.CoreConstant.DateTimeFormat;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.DataDimensionUtil;
import org.cloud.utils.MyBatisPlusUtil;
import org.cloud.vo.CommonApiResult;
import org.common.CommonPage;
import org.common.CommonParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.unknow.first.imexport.constant.ImexportMenuConstants.MENU_USER_EXCEL_PARENT;
import static com.unknow.first.imexport.constant.ImexportMenuConstants.MENU_USER_IMEXPORT_TASK_PAGE;
import static com.unknow.first.mongo.vo.MongoDBEnum.metadataFileAuthRangePersonal;

@RestController
@RequestMapping("/user/imexport/task")
@Api(description = "用户：导入导出任务管理", tags = "用户：导入导出任务管理")
@SystemResource(path = "/user/imexport/task", parentMenuCode = MENU_USER_EXCEL_PARENT, parentMenuName = "导入导出管理")
public class ImexportUserController {

    @Autowired
    FrameImportExportTaskService importExportTaskService;

    @ApiOperation(value = "创建导入导出任务", notes = "创建导入导出任务")
    @PostMapping()
    @SystemResource(value = "/create", description = "创建导入导出任务", authMethod = AuthMethod.BYUSERPERMISSION)
    public FrameImportExportTask create(ImportExportTaskCreateDTO exportTaskCreateDTO, TaskType taskType,
        @ApiParam("需要导入的文件，上传时必传") @RequestPart(required = false, name = "file") MultipartFile file) throws Exception {

        Assert.isTrue(StringUtils.hasLength(exportTaskCreateDTO.getBelongMicroservice()), "system.error.imexport.task.belongService.notEmpty");

        LoginUserDetails userDetails = RequestContextManager.single().getRequestContext().getUser();
        FrameImportExportTask importExportTaskCreate = new FrameImportExportTask();
        String fileName = null;
        if (TaskType.IMPORT.value == taskType.value) {
            Assert.notNull(file, "system.error.import.file.notEmpty");
            ObjectId fileId = MongoDBUtil.single().storeFile(userDetails, metadataFileAuthRangePersonal.value(), file);
            importExportTaskCreate.setFileId(fileId.toString());

            fileName = String.format("%s-%s-%d-%s-%s", "IMPORT", exportTaskCreateDTO.getTaskName(), userDetails.getId(), userDetails.getUsername(),
                file.getOriginalFilename());
        }
        BeanUtils.copyProperties(exportTaskCreateDTO, importExportTaskCreate);
        if (TaskType.EXPORT.value == taskType.value) {
            fileName = String.format("%s-%s-%d-%s-%s.%s", "EXPORT", exportTaskCreateDTO.getTaskName(), userDetails.getId(), userDetails.getUsername(),
                DateTimeFormat.FULLDATETIME_NO_SPLIT.getDateFormat().format(new Date()), exportTaskCreateDTO.getExtension());
        }
        importExportTaskCreate.setFileName(fileName);
        importExportTaskCreate.setTaskType(taskType.value);
        Map<String, Set<String>> dataDimension = DataDimensionUtil.single().getCurrentUserAllDataDimension();
        if (dataDimension != null) {
            importExportTaskCreate.setDataDimension(JSON.toJSONString(dataDimension));
        }
        if (importExportTaskService.save(importExportTaskCreate)) {
            return importExportTaskService.getById(importExportTaskCreate.getTaskId());
        }
        return null;
    }

    @ApiOperation(value = "查询导入导出任务列表", notes = "查询导入导出任务列表")
    @GetMapping()
    @SystemResource(value = "/list", description = "查询导入导出任务列表", authMethod = AuthMethod.ALLSYSTEMUSER, menuName = "我的导入导出", menuCode = MENU_USER_IMEXPORT_TASK_PAGE)
    public CommonApiResult<CommonPage<FrameImportExportTask>> list(FrameImportExportTaskQueryDTO queryDTO, @Validated CommonParam pageParam) {
        queryDTO.setCreateBy(RequestContextManager.single().getRequestContext().getUser().getId());
        QueryWrapper<FrameImportExportTask> queryWrapper = MyBatisPlusUtil.single().getPredicate(queryDTO);
        PageHelper.startPage(pageParam.getPage(), pageParam.getLimit(), pageParam.getSorts());
        List<FrameImportExportTask> frameImportExportTasks = importExportTaskService.list(queryWrapper);
        return CommonApiResult.createSuccessResult(CommonPage.restPage(frameImportExportTasks));
    }
}
