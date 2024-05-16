package com.unknow.first.imexport.conntroller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.unknow.first.api.common.CommonPage;
import com.unknow.first.api.common.CommonParam;
import com.unknow.first.imexport.constant.ImexportConstants.TaskType;
import com.unknow.first.imexport.domain.FrameImportExportTask;
import com.unknow.first.imexport.dto.FrameImportExportTaskQueryDTO;
import com.unknow.first.imexport.dto.ImportExportTaskCreateDTO;
import com.unknow.first.imexport.service.FrameImportExportTaskService;
import com.unknow.first.imexport.service.impl.ImexportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.cloud.constant.CoreConstant.AuthMethod;
import org.cloud.context.RequestContextManager;
import org.cloud.dimension.annotation.SystemResource;
import org.cloud.mybatisplus.utils.MyBatisPlusUtil;
import org.cloud.vo.CommonApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.unknow.first.imexport.constant.ImexportMenuConstants.MENU_USER_EXCEL_PARENT;
import static com.unknow.first.imexport.constant.ImexportMenuConstants.MENU_USER_IMEXPORT_TASK_PAGE;

@RestController
@RequestMapping("/user/imexport/task")
@Api(description = "用户：导入导出任务管理", tags = "用户：导入导出任务管理")
@SystemResource(path = "/user/imexport/task", parentMenuCode = MENU_USER_EXCEL_PARENT, parentMenuName = "导入导出管理")
public class ImexportUserController {

    @Autowired
    FrameImportExportTaskService importExportTaskService;
    @Autowired
    private ImexportService imexportService;

    @ApiOperation(value = "创建导入导出任务", notes = "创建导入导出任务")
    @PostMapping()
    @SystemResource(value = "/create", description = "创建导入导出任务", authMethod = AuthMethod.BYUSERPERMISSION)
    public FrameImportExportTask create(ImportExportTaskCreateDTO exportTaskCreateDTO, TaskType taskType,
        @ApiParam("需要导入的文件，上传时必传") @RequestPart(required = false, name = "file") MultipartFile file) throws Exception {
        return imexportService.create(exportTaskCreateDTO, taskType, file);

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
