package com.longyou.comm.admin.controller;

import com.longyou.comm.admin.service.IUserAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.cloud.constant.CoreConstant;
import org.cloud.dimension.annotation.SystemResource;
import org.cloud.model.TFrameUser;
import org.cloud.vo.CommonApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin/user", produces = MediaType.APPLICATION_JSON_VALUE)
@SystemResource(path = "/admin/user")
@Api(tags = "后台管理-用户信息管理", value = "后台管理-用户信息管理")
public class UserAdminController {

    @Autowired
    IUserAdminService userAdminService;

    @PostMapping("/saveOrUpdate")
    @SystemResource(value = "saveOrUpdateUser", description = "增加或更新用户", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @ApiOperation("增加或更新用户")
    public CommonApiResult<Long> saveOrUpdate(@RequestBody TFrameUser frameUser) throws Exception {
        return CommonApiResult.createSuccessResult(userAdminService.saveOrUpdate(frameUser));
    }

    @GetMapping("/resetPassword/{userId}")
    @SystemResource(value = "resetPassword", description = "重置密码", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @ApiOperation("重置密码")
    public CommonApiResult<Long> resetPassword(@PathVariable("userId") Long userId) throws Exception {
        return CommonApiResult.createSuccessResult(userAdminService.resetPassword(userId));
    }

}
