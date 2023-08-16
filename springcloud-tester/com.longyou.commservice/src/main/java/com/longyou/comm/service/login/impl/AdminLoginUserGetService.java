package com.longyou.comm.service.login.impl;

import static org.cloud.constant.LoginTypeConstant._LOGIN_BY_ADMIN_USER;

import com.longyou.comm.service.IUserInfoService;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.cloud.constant.CoreConstant.OperateLogType;
import org.cloud.dimension.userinfo.LoginUserGetInterface;
import org.cloud.dimension.userinfo.LoginUserGetParamsDTO;
import org.cloud.entity.LoginUserDetails;
import org.cloud.logs.annotation.AuthLog;
import org.cloud.model.TFrameRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service(LoginUserGetInterface._LOGIN_USER_GET_PREFIX + _LOGIN_BY_ADMIN_USER)
@Slf4j
public class AdminLoginUserGetService implements LoginUserGetInterface {

    @Autowired
    IUserInfoService userInfoService;

    @Override
    @AuthLog(bizType = "getUserInfo", desc = "获取登录用户信息【后台管理登录】", operateLogType = OperateLogType.LOG_TYPE_BACKEND)
    public LoginUserDetails getUserInfo(LoginUserGetParamsDTO loginUserGetParamsDTO) throws Exception {
        loginUserGetParamsDTO.setLoginType(_LOGIN_BY_ADMIN_USER);
        LoginUserDetails loginUserDetails = userInfoService.getUserByNameForAuth(loginUserGetParamsDTO);
        if (loginUserDetails != null && (loginUserDetails.getRoles() == null || loginUserDetails.getRoles().isEmpty())) {
            TFrameRole tFrameRole = new TFrameRole();
            tFrameRole.setRoleName("User");
            loginUserDetails.setRoles(Collections.singletonList(tFrameRole));
        }
        return loginUserDetails;
    }
}
