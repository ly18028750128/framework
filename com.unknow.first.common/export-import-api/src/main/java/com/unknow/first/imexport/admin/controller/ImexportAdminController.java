package com.unknow.first.imexport.admin.controller;

import static com.unknow.first.imexport.constant.ImexportMenuConstants.MENU_ADMIN_IMEXPORT_TASK_PAGE;
import static com.unknow.first.imexport.constant.ImexportMenuConstants.MENU_USER_EXCEL_PARENT;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.unknow.first.api.common.CommonPage;
import com.unknow.first.api.common.CommonParam;
import com.unknow.first.imexport.domain.FrameImportExportTask;
import com.unknow.first.imexport.dto.FrameImportExportTaskQueryDTO;
import com.unknow.first.imexport.service.FrameImportExportTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.cloud.dimension.annotation.SystemResource;
import org.cloud.constant.CoreConstant.AuthMethod;
import org.cloud.mybatisplus.utils.MyBatisPlusUtil;
import org.cloud.vo.CommonApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/imexport/task")
@Api(description = "管理员：导入导出任务管理", tags = "管理员：导入导出任务管理")
@SystemResource(path = "/admin/imexport/task", parentMenuCode = MENU_USER_EXCEL_PARENT, parentMenuName = "导入导出管理")
public class ImexportAdminController {

    @Autowired
    FrameImportExportTaskService importExportTaskService;

    @ApiOperation(value = "查询导入导出任务列表", notes = "查询导入导出任务列表")
    @GetMapping(value = "/")
    @SystemResource(value = "/list", description = "管理员：查询导入导出任务列表", authMethod = AuthMethod.BYUSERPERMISSION, menuName = "导入导出任务列表", menuCode = MENU_ADMIN_IMEXPORT_TASK_PAGE)
    public CommonApiResult<CommonPage<FrameImportExportTask>> list(FrameImportExportTaskQueryDTO queryDTO, @Validated CommonParam pageParam) {
        QueryWrapper<FrameImportExportTask> queryWrapper = MyBatisPlusUtil.single().getPredicate(queryDTO);
        PageHelper.startPage(pageParam.getPage(), pageParam.getLimit(), pageParam.getSorts());
        List<FrameImportExportTask> frameImportExportTasks = importExportTaskService.list(queryWrapper);
        return CommonApiResult.createSuccessResult(CommonPage.restPage(frameImportExportTasks));
    }

}
