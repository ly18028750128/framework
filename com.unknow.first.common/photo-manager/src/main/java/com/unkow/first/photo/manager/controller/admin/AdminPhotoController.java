package com.unkow.first.photo.manager.controller.admin;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.unknow.first.api.common.CommonPage;
import com.unknow.first.api.common.CommonParam;
import com.unknow.first.api.common.CommonResult;
import com.unkow.first.photo.manager.mapper.TPhoto;
import com.unkow.first.photo.manager.service.TPhotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.cloud.logs.annotation.AuthLog;
import org.cloud.dimension.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@Api(description = "后台管理-图片管理", tags = "AdminPhotoController")
@Validated
@RestController
@RequestMapping("/admin/photo")
@SystemResource(path = "/admin/photo")
@Slf4j
public class AdminPhotoController {

    @Autowired
    private TPhotoService photoService;


    @ApiOperation(value = "获取图片列表")
    @SystemResource(value = "/admin-getPhotoList", description = "获取图片列表", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @RequestMapping(method = RequestMethod.GET, value = "/getPhotoList")
    public CommonResult<CommonPage<TPhoto>> get(CommonParam param, String photoType, Integer languageType) {
        QueryWrapper<TPhoto> where = new QueryWrapper<>();
        where.eq(!StringUtils.isEmpty(photoType), "photo_type", photoType);
        where.eq(languageType != null, "language_type", languageType);
        return CommonResult.success(photoService.findListByPage(param.getPage(), param.getLimit(), "sort desc", where));
    }

    @AuthLog(bizType = "system", desc = "添加图片")
    @ApiOperation(value = "添加图片")
    @SystemResource(value = "/admin-addPhoto", description = "添加图片", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @RequestMapping(method = RequestMethod.POST, value = "/addPhoto")
    public CommonResult<?> addPhoto(@RequestBody TPhoto vo) throws BusinessException {
        if (StringUtils.isEmpty(vo.getImageUrl())) {
            throw new BusinessException("图片不存在");
        }
        if (photoService.add(vo)) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @AuthLog(bizType = "system", desc = "修改图片")
    @ApiOperation(value = "修改图片")
    @SystemResource(value = "/admin-updatePhoto", description = "修改图片", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @RequestMapping(method = RequestMethod.POST, value = "/updatePhoto")
    public CommonResult<?> updatePhoto(@RequestBody TPhoto vo) throws BusinessException {
        if (StringUtils.isEmpty(vo.getImageUrl())) {
            throw new BusinessException("图片不存在");
        }
        if (photoService.updateData(vo) > 0) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }

    @AuthLog(bizType = "system", desc = "修改图片状态")
    @ApiOperation(value = "修改图片状态")
    @SystemResource(value = "/admin-updatePhotoStatus", description = "修改图片状态", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @RequestMapping(method = RequestMethod.POST, value = "/updatePhotoStatus")
    public CommonResult<?> updatePhoto(@NotNull Integer id, @NotNull Integer status) throws BusinessException {
        if (status != 0 && status != 1) {
            throw new BusinessException("非系统状态");
        }
        TPhoto photo = photoService.findById(id);
        if (ObjectUtils.isEmpty(photo)) {
            throw new BusinessException("图片不存在");
        }
        TPhoto update = new TPhoto();
        update.setId(id);
        update.setStatus(status);
        if (photoService.updateData(update) > 0) {
            return CommonResult.success();
        }
        return CommonResult.failed();
    }
}
