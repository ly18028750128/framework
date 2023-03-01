package com.longyou.comm.conntroller;

import static org.cloud.constant.CoreConstant.USER_LOGIN_SUCCESS_CACHE_KEY;

import brave.Tracer;
import com.longyou.comm.config.MicroAppConfig;
import com.longyou.comm.config.MicroAppConfigList;
import com.longyou.comm.dto.UserOperatorCheckDTO;
import com.longyou.comm.service.IUserInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.constant.CoreConstant.AuthMethod;
import org.cloud.constant.CoreConstant.UserCacheKey;
import org.cloud.context.RequestContextManager;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.model.TFrameRole;
import org.cloud.userinfo.LoginUserGetInterface;
import org.cloud.utils.SpringContextUtil;
import org.cloud.vo.CommonApiResult;
import org.cloud.vo.LoginUserGetParamsDTO;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "UserInfoController", tags = "用户管理")
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
            if (loginUserDetails != null && (loginUserDetails.getRoles() == null || loginUserDetails.getRoles().isEmpty())) {
                TFrameRole tFrameRole = new TFrameRole();
                tFrameRole.setRoleName("User");
                loginUserDetails.setRoles(Collections.singletonList(tFrameRole));
            }
        }
        return loginUserDetails;
    }

    @ApiOperation(value = "admin-用户更新密码", notes = "admin-用户更新密码")
    @GetMapping(value = "/updatePassWord")
    @SystemResource(value = "updatePassWordByUser", description = "用户更新密码", authMethod = CoreConstant.AuthMethod.ALLSYSTEMUSER)
    public ResponseResult updatePassWordByUser(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword)
        throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(userInfoService.updatePassword(oldPassword, newPassword));
        return responseResult;
    }

    @ApiOperation(value = "admin-禁用用户", notes = "admin-禁用用户")
    @GetMapping(value = "/disabledUser/{userId}")
    @SystemResource(value = "disabledUser", description = "禁用用户", authMethod = AuthMethod.BYUSERPERMISSION)
    public ResponseResult disabledUser(@PathVariable("userId") Long userId) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(userInfoService.disabledUser(userId));
        return responseResult;
    }

    @ApiOperation(value = "admin-启用用户", notes = "admin-启用用户")
    @GetMapping(value = "/enabledUser/{userId}")
    @SystemResource(value = "enabledUser", description = "启用用户", authMethod = AuthMethod.BYUSERPERMISSION)
    public ResponseResult enabledUser(@PathVariable("userId") Long userId) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(userInfoService.enabledUser(userId));
        return responseResult;
    }

    @Autowired
    RedisUtil redisUtil;

    /**
     * 校验当前用户的操作权限列表，所有的登录用户都可以调用,用于控制按钮.
     *
     * @param operateAuthList
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "校验当前用户的操作权限列表", notes = "校验当前用户的操作权限列表，所有的登录用户都可以调用,用于控制按钮")
    @PostMapping(value = "/checkCurrentUserOperator")
    @SystemResource(value = "checkCurrentUserOperator", description = "校验当前用户的操作权限列表", authMethod = AuthMethod.ALLSYSTEMUSER)
    public CommonApiResult<UserOperatorCheckDTO> checkCurrentUserOperator(@ApiParam("权限点列表，例如：common-service::/admin/user::saveOrUpdateUser") @RequestBody List<String> operateAuthList) throws Exception {
        Map<String, Boolean> checkResult = new LinkedHashMap<>(1);
        LoginUserDetails loginUserDetails = RequestContextManager.single().getRequestContext().getUser();
        Set<String> currentUserOperateAuthSet = redisUtil.hashGet(USER_LOGIN_SUCCESS_CACHE_KEY + loginUserDetails.getId(), UserCacheKey.FUNCTION.value());
        for (String operateAuth : operateAuthList) {
            checkResult.put(operateAuth, currentUserOperateAuthSet.contains(operateAuth));
        }
        return CommonApiResult.createSuccessResult(UserOperatorCheckDTO.builder().userId(loginUserDetails.getId()).checkResult(checkResult).build());
    }

}
