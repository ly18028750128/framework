package com.longyou.comm.admin.controller;

import com.longyou.comm.CommonServiceConst;
import com.longyou.comm.admin.service.IUserAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.cloud.dimension.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.model.TFrameUser;

import org.cloud.utils.EnvUtil;
import org.cloud.utils.MD5Encoder;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/user", produces = MediaType.APPLICATION_JSON_VALUE)
@SystemResource(path = "/admin/user")
@Api(tags = "后台管理-用户信息管理",value = "后台管理-用户信息管理")
public class UserAdminController {

    @Autowired
    IUserAdminService userAdminService;

    @PostMapping("/saveOrUpdate")
    @SystemResource(value = "saveOrUpdateUser", description = "增加或更新用户", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @ApiOperation("增加或更新用户")
    public ResponseResult saveOrUpdate(@RequestBody TFrameUser frameUser) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(userAdminService.saveOrUpdate(frameUser));
        return responseResult;
    }

    @GetMapping("/resetPassword/{userId}")
    @SystemResource(value = "resetPassword", description = "重置密码", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @ApiOperation("重置密码")
    public ResponseResult resetPassword(@PathVariable("userId") Long userId) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        final TFrameUser frameUser = new TFrameUser();
        final String restPass = EnvUtil.single().getEnv("system.setting.password.reset", "123456");  // 可以配置重置后的密码，如果没有配置那么重置后的密码为123456
        final String salt = EnvUtil.single().getEnv("spring.security.salt-password", "");
        frameUser.setId(userId);
        frameUser.setPassword(MD5Encoder.encode(restPass, salt));
        frameUser.setStatus(CommonServiceConst.userStatus.Reset.value());   // 状态设置为2，表示是密码重置
        responseResult.setData(userAdminService.saveOrUpdate(frameUser));
        return responseResult;
    }

}
