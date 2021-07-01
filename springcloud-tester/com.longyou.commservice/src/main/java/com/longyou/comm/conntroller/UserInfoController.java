package com.longyou.comm.conntroller;

import brave.Tracer;
import com.longyou.comm.config.MicroAppConfig;
import com.longyou.comm.config.MicroAppConfigList;
import com.longyou.comm.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.constant.CoreConstant.AuthMethod;
import org.cloud.entity.LoginUserDetails;
import org.cloud.userinfo.LoginUserGetInterface;
import org.cloud.utils.SpringContextUtil;
import org.cloud.vo.LoginUserGetParamsDTO;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/userinfo")
@SystemResource(path = "/userinfo")
@Slf4j
public class UserInfoController {

    @Autowired
    IUserInfoService userInfoService;
    @Autowired
    MicroAppConfigList microAppConfigList;

    @Autowired
    Tracer tracer;

    @RequestMapping(value = "/getUserByName", method = RequestMethod.POST)
    public LoginUserDetails getUserByName(@RequestBody LoginUserGetParamsDTO loginUserGetParamsDTO, HttpServletRequest request)
        throws Exception {
        LoginUserDetails loginUserDetails = null;
        LoginUserGetInterface loginUserGetInterface = null;
        //@TODO 这里要进行从数据库获取小程序或者公众号的配置，决定通过什么方式获取用户名，如，公众号，微信小程序，支付宝小程序等，目前只支持微信小程序，且配置文件配置在配置中心需更改到数据库
        if (loginUserGetParamsDTO.getMicroAppIndex() != null) {  // 如果没有传递小程序的序号，那么调用数据库进行处理，
            MicroAppConfig microAppConfig = microAppConfigList.getAppList().get(loginUserGetParamsDTO.getMicroAppIndex());
            loginUserGetInterface = SpringContextUtil
                .getBean(LoginUserGetInterface._LOGIN_USER_GET_PREFIX + microAppConfig.getType(), LoginUserGetInterface.class);
            loginUserDetails = loginUserGetInterface.getUserInfo(loginUserGetParamsDTO);
        } else if (loginUserGetParamsDTO.getLoginType() != null) {
            loginUserGetInterface = SpringContextUtil
                .getBean(LoginUserGetInterface._LOGIN_USER_GET_PREFIX + loginUserGetParamsDTO.getLoginType(), LoginUserGetInterface.class);
            loginUserDetails = loginUserGetInterface.getUserInfo(loginUserGetParamsDTO);
        } else {
            loginUserDetails = userInfoService.getUserByNameForAuth(loginUserGetParamsDTO);
            if (loginUserDetails.getRoles() == null || loginUserDetails.getRoles().isEmpty()) {
                loginUserDetails.setRoles(CollectionUtils.arrayToList(new String[]{"User"}));
            }
        }
        return loginUserDetails;
    }

    @GetMapping(value = "/updatePassWord")
    @SystemResource(value = "updatePassWordByUser", description = "用户更新密码", authMethod = CoreConstant.AuthMethod.ALLSYSTEMUSER)
    public ResponseResult updatePassWordByUser(@RequestParam("oldPassword") String oldPassword,
        @RequestParam("newPassword") String newPassword) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(userInfoService.updatePassword(oldPassword, newPassword));
        return responseResult;
    }

    @GetMapping(value = "/disabledUser/{userId}")
    @SystemResource(value = "disabledUser", description = "禁用用户", authMethod = AuthMethod.BYUSERPERMISSION)
    public ResponseResult updatePassWordByUser(@PathVariable("userId") Long userId) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(userInfoService.disabledUser(userId));
        return responseResult;
    }

}
