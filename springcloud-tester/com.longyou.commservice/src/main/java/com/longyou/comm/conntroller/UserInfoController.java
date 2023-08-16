package com.longyou.comm.conntroller;

import static org.cloud.constant.CoreConstant.USER_LOGIN_SUCCESS_CACHE_KEY;
import static org.cloud.constant.CoreConstant._USER_TYPE_KEY;
import static org.cloud.constant.LoginTypeConstant._LOGIN_BY_ADMIN_USER;

import brave.Tracer;
import cn.hutool.core.util.StrUtil;
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
import lombok.extern.slf4j.Slf4j;
import org.cloud.constant.CoreConstant;
import org.cloud.constant.CoreConstant.AuthMethod;
import org.cloud.constant.CoreConstant.UserCacheKey;
import org.cloud.constant.LoginTypeConstant.DataShow;
import org.cloud.constant.LoginTypeConstant.LoginTypeEnum;
import org.cloud.context.RequestContextManager;
import org.cloud.core.redis.RedisUtil;
import org.cloud.dimension.annotation.SystemResource;
import org.cloud.dimension.userinfo.LoginUserGetInterface;
import org.cloud.dimension.userinfo.LoginUserGetParamsDTO;
import org.cloud.entity.LoginUserDetails;
import org.cloud.model.TFrameRole;
import org.cloud.utils.SpringContextUtil;
import org.cloud.vo.CommonApiResult;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
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
    public LoginUserDetails getUserByName(@RequestBody LoginUserGetParamsDTO loginUserGetParamsDTO) throws Exception {
        LoginUserGetInterface loginUserGetInterface = getLoginUserBean(loginUserGetParamsDTO);
        return loginUserGetInterface.getUserInfo(loginUserGetParamsDTO);
    }

    private LoginUserGetInterface getLoginUserBean(LoginUserGetParamsDTO loginUserGetParamsDTO) {
        String beanType = _LOGIN_BY_ADMIN_USER;
        //@TODO 这里要进行从数据库获取小程序或者公众号的配置，决定通过什么方式获取用户名，如，公众号，微信小程序，支付宝小程序等，目前只支持微信小程序，且配置文件配置在配置中心需更改到数据库
        if (loginUserGetParamsDTO.getMicroAppIndex() != null) {  // 如果没有传递小程序的序号，那么调用数据库进行处理，
            MicroAppConfig microAppConfig = microAppConfigList.getAppList().get(loginUserGetParamsDTO.getMicroAppIndex());
            beanType = microAppConfig.getType();
        } else if (loginUserGetParamsDTO.getLoginType() != null) {
            beanType = loginUserGetParamsDTO.getLoginType();
        }
        final LoginTypeEnum loginTypeEnum = LoginTypeEnum.forCode(beanType);
        Assert.notNull(loginTypeEnum, "不支持的登录方式");
        loginUserGetParamsDTO.getParamMap().put(_USER_TYPE_KEY, loginTypeEnum.userType);
        return SpringContextUtil.getBean(LoginUserGetInterface._LOGIN_USER_GET_PREFIX + beanType);
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

    @ApiOperation(value = "admin-管理员修改指定用户密码", notes = "admin-管理员修改指定用户密码")
    @GetMapping(value = "/updatePassWordByAdmin")
    @SystemResource(value = "updatePassWordByAdmin", description = "管理员修改指定用户密码", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    public ResponseResult updatePassWordByAdmin(@RequestParam("id") Long id, @RequestParam("newPassword") String newPassword) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(userInfoService.updatePasswordByAdmin(id, newPassword));
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
    public CommonApiResult<UserOperatorCheckDTO> checkCurrentUserOperator(
        @ApiParam("权限点列表，例如：common-service::/admin/user::saveOrUpdateUser") @RequestBody List<String> operateAuthList) throws Exception {
        Map<String, Boolean> checkResult = new LinkedHashMap<>(1);
        LoginUserDetails loginUserDetails = RequestContextManager.single().getRequestContext().getUser();
        Set<String> currentUserOperateAuthSet = redisUtil.hashGet(USER_LOGIN_SUCCESS_CACHE_KEY + loginUserDetails.getId(), UserCacheKey.FUNCTION.value());
        for (String operateAuth : operateAuthList) {
            checkResult.put(operateAuth, currentUserOperateAuthSet.contains(operateAuth));
        }
        return CommonApiResult.createSuccessResult(UserOperatorCheckDTO.builder().userId(loginUserDetails.getId()).checkResult(checkResult).build());
    }

    @ApiOperation(value = "admin-获取用户类型", notes = "admin-获取用户类型")
    @GetMapping(value = "/userType/{canCreate}")
    @SystemResource(value = "enabledUser", description = "启用用户", authMethod = AuthMethod.BYUSERPERMISSION)
    public CommonApiResult<DataShow> enabledUser(@PathVariable("canCreate") boolean canCreate) throws Exception {
        return CommonApiResult.createSuccessResult(LoginTypeEnum.getShowList(canCreate));
    }

}
