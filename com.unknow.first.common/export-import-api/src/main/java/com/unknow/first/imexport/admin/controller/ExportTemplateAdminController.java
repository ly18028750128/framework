package com.unknow.first.imexport.admin.controller;

import static com.unknow.first.imexport.constant.ImexportMenuConstants.MENU_EXPORT_TEMPLATE_PAGE;
import static com.unknow.first.imexport.constant.ImexportMenuConstants.MENU_USER_EXCEL_PARENT;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.unknow.first.api.common.CommonPage;
import com.unknow.first.api.common.CommonParam;
import com.unknow.first.imexport.domain.FrameExportTemplate;
import com.unknow.first.imexport.dto.FrameExportTemplateQueryDTO;
import com.unknow.first.imexport.service.FrameExportTemplateService;
import com.unknow.first.mongo.dto.MetadataDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Objects;
import org.cloud.dimension.annotation.SystemResource;
import org.cloud.constant.CoreConstant.AuthMethod;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;
import org.cloud.mybatisplus.utils.MyBatisPlusUtil;
import org.cloud.vo.CommonApiResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/admin/export/template")
@Api(description = "数据导出模板管理", tags = "数据导出模板管理")
@SystemResource(path = "/admin/export/template", parentMenuCode = MENU_USER_EXCEL_PARENT, parentMenuName = "导入导出管理")
public class ExportTemplateAdminController {

    @Autowired
    FrameExportTemplateService exportTemplateService;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @ApiOperation(value = "管理员：查询导出模板列表", notes = "管理员：查询导出模板列表")
    @GetMapping(value = "/")
    @SystemResource(value = "/list", description = "查询导出模板列表", authMethod = AuthMethod.BYUSERPERMISSION, menuName = "导出模板管理", menuCode = MENU_EXPORT_TEMPLATE_PAGE)
    public CommonApiResult<CommonPage<FrameExportTemplate>> list(FrameExportTemplateQueryDTO queryDTO, @Validated CommonParam pageParam) {
        QueryWrapper<FrameExportTemplate> queryWrapper = MyBatisPlusUtil.single().getPredicate(queryDTO);
        PageHelper.startPage(pageParam.getPage(), pageParam.getLimit(), pageParam.getSorts());
        List<FrameExportTemplate> frameExportTemplates = exportTemplateService.list(queryWrapper);
        return CommonApiResult.createSuccessResult(CommonPage.restPage(frameExportTemplates));
    }

    @ApiOperation(value = "管理员：创建或更新导出模板", notes = "管理员：创建或更新导出模板")
    @PostMapping(value = "/")
    @SystemResource(value = "/createOrUpdate", description = "创建或更新导出模板", authMethod = AuthMethod.BYUSERPERMISSION)
    public CommonApiResult<FrameExportTemplate> createOrUpdate(FrameExportTemplate frameExportTemplate,
        @ApiParam(value = "模板文件", required = true) @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {

        if (frameExportTemplate.getTemplateId() == null) {
            Assert.notNull(file, "请上传文件！");
        }
        if (file != null) {
            final int suffixIndex = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".");
            MetadataDTO metadataDTO = new MetadataDTO();
            LoginUserDetails user = RequestContextManager.single().getRequestContext().getUser();
            metadataDTO.setOwner(user.getId());
            metadataDTO.setOwnerName(user.getUsername());
            metadataDTO.setOwnerFullName(user.getFullName());
            if (suffixIndex > -1) {
                metadataDTO.setSuffix(file.getOriginalFilename().substring(suffixIndex));
            }
            String contentType = file.getContentType() == null ? "unknown" : file.getContentType();
            metadataDTO.setContentType(contentType);
            String templateFileId = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), contentType, metadataDTO).toString();
            frameExportTemplate.setFileId(templateFileId);
        }
        exportTemplateService.saveOrUpdate(frameExportTemplate);
        return CommonApiResult.createSuccessResult(frameExportTemplate);

    }
}
