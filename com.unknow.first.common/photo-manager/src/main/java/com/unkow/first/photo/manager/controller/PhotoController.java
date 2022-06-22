package com.unkow.first.photo.manager.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.juna.ruiqi.api.CommonResult;
import com.juna.ruiqi.constants.CommonConstants;
import com.unkow.first.photo.manager.mapper.TPhoto;
import com.unkow.first.photo.manager.service.TPhotoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.cloud.annotation.SystemResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(description = "图片接口", tags = "PhotoController")
@Validated
@RestController
@RequestMapping("/photo/info")
@SystemResource(path = "/photo/info")
public class PhotoController {

    @Autowired
    private TPhotoService photoService;

    @ApiOperation("获取图片信息")
    @RequestMapping(method = RequestMethod.GET, value = "/getPhotoList")
    public CommonResult<List<TPhoto>> getHomeBanner(@RequestParam(value = "languageType", defaultValue = "1") Integer languageType,
        @RequestParam("photoType") String photoType) {
        QueryWrapper<TPhoto> where = new QueryWrapper<>();
        where.select("image_url,jump_url,photo_desc");
        where.eq("status", CommonConstants.StatusEnum.NORMAL.getStatus());
        where.eq("photo_type", photoType);
        where.eq("language_type", languageType);
        List<TPhoto> photos = photoService.findListByPage(1, 200, "sort desc", where).getList();
        return CommonResult.success(photos);
    }
}
