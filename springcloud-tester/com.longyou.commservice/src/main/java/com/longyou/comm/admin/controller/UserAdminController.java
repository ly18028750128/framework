package com.longyou.comm.admin.controller;

import com.longyou.comm.admin.service.IUserAdminService;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.model.TFrameUser;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin/user", produces = MediaType.APPLICATION_JSON_VALUE)
@SystemResource(path = "/admin/user")
public class UserAdminController {

    @Autowired
    IUserAdminService userAdminService;

    @PostMapping("/saveOrUpdate")
    @SystemResource(value = "saveOrUpdateUser", description = "用户角色授权", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    public ResponseResult saveOrUpdate(@RequestBody TFrameUser frameUser) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(userAdminService.saveOrUpdate(frameUser));
        return responseResult;
    }
}
