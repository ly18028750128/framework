package com.longyou.comm.admin.controller;

import com.longyou.comm.admin.service.IRoleAdminService;
import org.cloud.model.TFrameRole;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/admin/role/")
@RestController
public class FrameRoleController {

    @Autowired
    IRoleAdminService roleAdminService;

    @PostMapping("/saveOrUpdate")
    public ResponseResult saveOrUpdateRoles(@RequestBody TFrameRole frameRole) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        roleAdminService.saveOrUpdate(frameRole);
        return responseResult;
    }

}
