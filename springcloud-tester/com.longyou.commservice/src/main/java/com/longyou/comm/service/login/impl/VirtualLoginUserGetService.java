package com.longyou.comm.service.login.impl;

import static org.cloud.constant.LoginTypeConstant._LOGIN_BY_VIRTUAL_USER;

import com.longyou.comm.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.cloud.constant.CoreConstant.OperateLogType;
import org.cloud.dimension.userinfo.LoginUserGetInterface;
import org.cloud.dimension.userinfo.LoginUserGetParamsDTO;
import org.cloud.entity.LoginUserDetails;
import org.cloud.logs.annotation.AuthLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service(LoginUserGetInterface._LOGIN_USER_GET_PREFIX + _LOGIN_BY_VIRTUAL_USER)
@Slf4j
public class VirtualLoginUserGetService implements LoginUserGetInterface {

    @Autowired
    IUserInfoService userInfoService;

    @Override
    @AuthLog(bizType = "getUserInfo", desc = "获取登录用户信息【虚拟用户登录】", operateLogType = OperateLogType.LOG_TYPE_BACKEND)
    public LoginUserDetails getUserInfo(LoginUserGetParamsDTO loginUserGetParamsDTO) throws Exception {
        return userInfoService.getUserByNameForAuth(loginUserGetParamsDTO);
    }
}
