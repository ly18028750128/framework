package com.longyou.comm.conntroller;

import static org.cloud.config.MfaFilterConfig.__MFA_TOKEN_USER_CACHE_KEY;
import static org.cloud.config.MfaFilterConfig.__MFA_TOKEN_USER_GOOGLE_SECRET_CACHE_KEY;

import com.longyou.comm.service.FrameUserRefService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.cloud.annotation.MfaAuth;
import org.cloud.annotation.SystemResource;
import org.cloud.common.service.AESService;
import org.cloud.constant.CoreConstant;
import org.cloud.constant.CoreConstant.AuthMethod;
import org.cloud.constant.MfaConstant;
import org.cloud.context.RequestContext;
import org.cloud.context.RequestContextManager;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.exception.BusinessException;
import org.cloud.utils.CommonUtil;
import org.cloud.utils.GoogleAuthenticatorUtil;
import org.cloud.utils.SpringContextUtil;
import org.cloud.vo.CommonApiResult;
import org.cloud.vo.FrameUserRefVO;
import org.cloud.vo.ResponseResult;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/mfa")
@SystemResource(path = "/common/user/mfa")
@Api(value = "UserMfaController", tags = "双因子验证API")
public class UserMfaController {

    @Autowired
    private FrameUserRefService frameUserRefService;

    /**
     * 校验谷歌验证码是否已经绑定
     *
     * @return
     * @throws Exception
     */
    @ApiOperation("校验谷歌验证码是否已经绑定")
    @GetMapping("/checkUserGoogleSecretBindStatus")
    @SystemResource(value = "checkUserGoogleSecretBindStatus", description = "校验谷歌验证绑定状态")
    public ResponseResult checkUserGoogleSecretBindStatus() throws Exception {
        LoginUserDetails loginUserDetails = RequestContextManager.single().getRequestContext().getUser();
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        FrameUserRefVO frameUserRefVO = frameUserRefService.getUserRefByAttributeName(loginUserDetails.getId(),
            MfaConstant._GOOGLE_MFA_USER_SECRET_REF_FlAG_ATTR_NAME.value());
        final Map<String, Object> returnData = new LinkedHashMap<>();
        if (frameUserRefVO != null && "true".equals(frameUserRefVO.getAttributeValue())) {
            // 如果已经绑定
            returnData.put(MfaConstant._GOOGLE_MFA_USER_SECRET_REF_FlAG_ATTR_NAME.value(), true);
        } else {
            frameUserRefVO = GoogleAuthenticatorUtil.single().createNewUserRefVO(loginUserDetails);
            frameUserRefService.create(frameUserRefVO);
            AESService aesService = SpringContextUtil.getBean(AESService.class);
            final String googleSecret = aesService.decrypt(frameUserRefVO.getAttributeValue());
            returnData.put(MfaConstant._GOOGLE_MFA_USER_SECRET_REF_FlAG_ATTR_NAME.value(), false);
            returnData.put("description", MfaConstant.CORRELATION_YOUR_GOOGLE_KEY.description());
            returnData.put("secret", googleSecret);
            returnData.put("secretQRBarcode", GoogleAuthenticatorUtil.single().getQRBarcode(loginUserDetails.getUsername(), googleSecret));
            returnData.put("secretQRBarcodeURL", GoogleAuthenticatorUtil.single().getQRBarcodeURL(loginUserDetails.getUsername(), "", googleSecret));
            RedisUtil.single().set(__MFA_TOKEN_USER_GOOGLE_SECRET_CACHE_KEY + loginUserDetails.getId(), frameUserRefVO.getAttributeValue());
        }
        responseResult.setData(returnData);
        return responseResult;
    }

    /**
     * 绑定谷歌验证码
     *
     * @return
     * @throws Exception
     */
    @ApiOperation("绑定谷歌验证")
    @GetMapping("/bindUserGoogleSecret")
    @SystemResource(value = "bindUserGoogleSecret", description = "绑定谷歌验证")
    public ResponseResult bindUserGoogleSecret() throws Exception {

        // 绑定后先验证下，避免绑定出错的情况
        String googleSecret = GoogleAuthenticatorUtil.single().getCurrentUserVerifyKey();
        if (!GoogleAuthenticatorUtil.single().checkGoogleVerifyCode(googleSecret)) {
            throw new BusinessException("system.error.google.valid", 400);
        }

        LoginUserDetails loginUserDetails = RequestContextManager.single().getRequestContext().getUser();
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        FrameUserRefVO frameUserRefVO = frameUserRefService.getUserRefByAttributeName(loginUserDetails.getId(),
            MfaConstant._GOOGLE_MFA_USER_SECRET_REF_FlAG_ATTR_NAME.value());

        if (frameUserRefVO == null) {
            frameUserRefVO = new FrameUserRefVO();
            frameUserRefVO.setUserId(loginUserDetails.getId());
            frameUserRefVO.setAttributeName(MfaConstant._GOOGLE_MFA_USER_SECRET_REF_FlAG_ATTR_NAME.value());
            frameUserRefVO.setAttributeValue("true");
            frameUserRefVO.setRemark(MfaConstant._GOOGLE_MFA_USER_SECRET_REF_FlAG_ATTR_NAME.description());
            frameUserRefService.create(frameUserRefVO);
        } else {
            frameUserRefVO.setAttributeValue("true");
            frameUserRefService.update(frameUserRefVO);
        }
        responseResult.setData(frameUserRefVO);
        return responseResult;
    }


    @Value("${system.mfa.expired-time:1800}")
    Long expiredTime;

    /**
     * 校验谷歌验证码绑定状态
     *
     * @return
     * @throws Exception
     */
    @ApiOperation("校验当前用户的谷歌验证码")
    @GetMapping("/checkCurrentUserGoogleCode/{mfaValue}")
    @SystemResource(value = "checkCurrentUserGoogleCode", description = "校验当前用户的谷歌验证码", authMethod = CoreConstant.AuthMethod.ALLSYSTEMUSER)
    public ResponseResult checkCurrentUserGoogleCode(@PathVariable String mfaValue, HttpServletRequest httpServletRequest) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        RequestContext currentRequestContext = RequestContextManager.single().getRequestContext();
        LoginUserDetails user = currentRequestContext.getUser();
        String googleSecret = GoogleAuthenticatorUtil.single().getCurrentUserVerifyKey();
        final Boolean isValidatePass = GoogleAuthenticatorUtil.single().checkGoogleVerifyCode(googleSecret, mfaValue);
        String ipHash = RedisUtil.single().getMd5Key(CommonUtil.single().getIpAddress(httpServletRequest));
        RedisUtil.single().set(__MFA_TOKEN_USER_CACHE_KEY + user.getId() + ":" + ipHash, isValidatePass, expiredTime);
        return responseResult;
    }

    /**
     * 重置谷歌验证码绑定状态
     *
     * @return
     * @throws Exception
     */
    @ApiOperation("管理员：重置谷歌验证码状态")
    @GetMapping("/resetBindUserGoogleSecretFlag/{userId}")
    @SystemResource(value = "resetBindUserGoogleSecretFlag", description = "重置谷歌验证码状态", authMethod = AuthMethod.BYUSERPERMISSION)
    public ResponseResult resetBindUserGoogleFlag(@PathVariable("userId") Long userId) throws Exception {
        return resetUserGoogle(userId);
    }

    /**
     * 重置谷歌验证码绑定状态
     *
     * @return
     * @throws Exception
     */
    @ApiOperation("重置自己的谷歌验证码")
    @GetMapping("/resetMyGoogleSecretFlag")
    @SystemResource(value = "resetMyGoogleSecretFlag", description = "重置自己的谷歌验证码", authMethod = AuthMethod.ALLSYSTEMUSER)
    @MfaAuth
    public ResponseResult resetMyGoogleSecretFlag() throws Exception {
        return resetUserGoogle(RequestContextManager.single().getRequestContext().getUser().getId());
    }

    @NotNull
    private ResponseResult resetUserGoogle(Long userId) {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        FrameUserRefVO frameUserRefVO = frameUserRefService.getUserRefByAttributeName(userId, MfaConstant._GOOGLE_MFA_USER_SECRET_REF_FlAG_ATTR_NAME.value());
        if (frameUserRefVO != null) {
            frameUserRefService.delete(frameUserRefVO.getId());
        }
        frameUserRefVO = frameUserRefService.getUserRefByAttributeName(userId, MfaConstant._GOOGLE_MFA_USER_SECRET_REF_ATTR_NAME.value());
        if (frameUserRefVO != null) {
            frameUserRefService.delete(frameUserRefVO.getId());
        }
        RedisTemplate redisTemplate = RedisUtil.single().getRedisTemplate();
        Set<?> keys = redisTemplate.keys(__MFA_TOKEN_USER_CACHE_KEY + userId + ":*");
        if (CollectionUtils.isNotEmpty(keys)) {
            redisTemplate.delete(keys);
        }
        return responseResult;
    }

    @ApiOperation("验证校验谷歌的测试API")
    @GetMapping("/checkAspectGoogleMfa")
    @MfaAuth
    @SystemResource(value = "checkAspectGoogleMfa", description = "验证校验谷歌的测试API", authMethod = AuthMethod.ALLSYSTEMUSER)
    public CommonApiResult<Boolean> checkAspectGoogleMfa() throws Exception {
        return CommonApiResult.createSuccessResult(true);
    }

}
