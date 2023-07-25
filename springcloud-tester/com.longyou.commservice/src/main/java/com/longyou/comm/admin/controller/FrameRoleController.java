package com.longyou.comm.admin.controller;

import com.longyou.comm.admin.service.IRoleAdminService;
import org.cloud.dimension.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.model.TFrameRole;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin/role/")
@RestController
@SystemResource(path = "/admin/role/")
public class FrameRoleController {

    @Autowired
    IRoleAdminService roleAdminService;

    @PostMapping("/saveOrUpdate")
    @SystemResource(value = "saveOrUpdateRoles", description = "操作权限授权", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    public ResponseResult saveOrUpdateRoles(@RequestBody TFrameRole frameRole) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        roleAdminService.saveOrUpdate(frameRole);
        return responseResult;
    }
}
