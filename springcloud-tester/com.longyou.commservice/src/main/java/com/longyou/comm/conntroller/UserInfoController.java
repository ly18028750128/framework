package com.longyou.comm.conntroller;

import com.longyou.comm.config.MicroAppConfig;
import com.longyou.comm.config.MicroAppConfigList;
import com.longyou.comm.service.IUserInfoService;
import org.cloud.entity.LoginUserDetails;
import org.cloud.userinfo.LoginUserGetInterface;
import org.cloud.utils.SpringContextUtil;
import org.cloud.vo.LoginUserGetParamsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/userinfo")
public class UserInfoController {
    @Autowired
    IUserInfoService userInfoService;
    @Autowired
    MicroAppConfigList microAppConfigList;

    @RequestMapping(value = "/getUserByName", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public LoginUserDetails getUserByName(HttpServletRequest request, @RequestBody LoginUserGetParamsDTO loginUserGetParamsDTO) throws Exception {
        LoginUserDetails loginUserDetails = null;
        if (loginUserGetParamsDTO.getMicroAppIndex() == null) {  // 如果没有传递小程序的序号，那么调用数据库进行处理
            Map<String,Object> userQueryParams = new HashMap<>();
            loginUserDetails = userInfoService.getUserByNameForAuth(loginUserGetParamsDTO);
            if (loginUserDetails.getRoles() == null || loginUserDetails.getRoles().isEmpty()) {
                loginUserDetails.setRoles(CollectionUtils.arrayToList(new String[]{"User"}));
            }
        } else {
            MicroAppConfig microAppConfig = microAppConfigList.getAppList().get(loginUserGetParamsDTO.getMicroAppIndex());
            LoginUserGetInterface loginUserGetInterface = SpringContextUtil.getBean(LoginUserGetInterface._LOGIN_USER_GET_PREFIX + microAppConfig.getType(), LoginUserGetInterface.class);
            loginUserDetails = loginUserGetInterface.getUserInfo(loginUserGetParamsDTO);
        }

        return loginUserDetails;
    }



}
