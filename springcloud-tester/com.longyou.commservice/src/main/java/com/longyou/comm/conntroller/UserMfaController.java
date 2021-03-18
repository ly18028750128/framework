package com.longyou.comm.conntroller;

import static org.cloud.config.MfaFilterConfig.__MFA_TOKEN_USER_CACHE_KEY;
import static org.cloud.config.MfaFilterConfig.__MFA_TOKEN_USER_GOOGLE_SECRET_CACHE_KEY;

import com.longyou.comm.service.FrameUserRefService;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.constant.MfaConstant;
import org.cloud.context.RequestContext;
import org.cloud.context.RequestContextManager;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.CommonUtil;
import org.cloud.utils.GoogleAuthenticatorUtil;
import org.cloud.vo.FrameUserRefVO;
import org.cloud.vo.ResponseResult;
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
public class UserMfaController {

  @Autowired
  private FrameUserRefService frameUserRefService;


  /**
   * 校验谷歌验证码是否已经绑定
   *
   * @return
   * @throws Exception
   */
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
      returnData.put(MfaConstant._GOOGLE_MFA_USER_SECRET_REF_FlAG_ATTR_NAME.value(), false);
      returnData.put("description", MfaConstant.CORRELATION_YOUR_GOOGLE_KEY.description());
      returnData.put("secret", frameUserRefVO.getAttributeValue());
      returnData.put("secretQRBarcode",
          GoogleAuthenticatorUtil.single().getQRBarcode(loginUserDetails.getUsername(), frameUserRefVO.getAttributeValue()));
      returnData.put("secretQRBarcodeURL",
          GoogleAuthenticatorUtil.single().getQRBarcodeURL(loginUserDetails.getUsername(), "", frameUserRefVO.getAttributeValue()));
      redisUtil.set(__MFA_TOKEN_USER_GOOGLE_SECRET_CACHE_KEY + loginUserDetails.getId(), frameUserRefVO.getAttributeValue());
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
  @GetMapping("/bindUserGoogleSecret")
  @SystemResource(value = "bindUserGoogleSecret", description = "绑定谷歌验证")
  public ResponseResult bindUserGoogleSecret() throws Exception {
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
      frameUserRefVO.setCreateBy(loginUserDetails.getUsername());
      frameUserRefVO.setUpdateBy(loginUserDetails.getUsername());
      frameUserRefService.create(frameUserRefVO);
    } else {
      frameUserRefVO.setAttributeValue("true");
      frameUserRefService.update(frameUserRefVO);
    }
    responseResult.setData(frameUserRefVO);
    return responseResult;
  }

  /**
   * 重置谷歌验证码绑定状态
   *
   * @return
   * @throws Exception
   */
  @GetMapping("/resetBindUserGoogleSecretFlag/{userId}")
  @SystemResource(value = "resetBindUserGoogleSecretFlag", description = "重置谷歌验证码状态", authMethod =
      CoreConstant.AuthMethod.BYUSERPERMISSION)
  public ResponseResult resetBindUserGoogleFlag(@PathVariable("userId") Long userId) throws Exception {
    ResponseResult responseResult = ResponseResult.createSuccessResult();
    FrameUserRefVO frameUserRefVO = frameUserRefService.getUserRefByAttributeName(userId,
        MfaConstant._GOOGLE_MFA_USER_SECRET_REF_FlAG_ATTR_NAME.value());
    if (frameUserRefVO != null) {
      frameUserRefService.delete(frameUserRefVO.getId());
    }

    frameUserRefVO = frameUserRefService.getUserRefByAttributeName(userId,
        MfaConstant._GOOGLE_MFA_USER_SECRET_REF_ATTR_NAME.value());
    if (frameUserRefVO != null) {
      frameUserRefService.delete(frameUserRefVO.getId());
    }

    RedisTemplate redisTemplate = redisUtil.getRedisTemplate();

    Set keys = redisTemplate.keys(__MFA_TOKEN_USER_CACHE_KEY + userId + ":*");

    if (CollectionUtils.isNotEmpty(keys)) {
      redisTemplate.delete(keys);
    }

//    redisUtil.remove(__MFA_TOKEN_USER_CACHE_KEY + userId);
    return responseResult;
  }

  @Autowired
  RedisUtil redisUtil;

  @Value("${system.mfa.expired-time:1800}")
  Long expiredTime;

  /**
   * 校验谷歌验证码绑定状态
   *
   * @return
   * @throws Exception
   */
  @GetMapping("/checkCurrentUserGoogleCode/{mfaValue}")
  @SystemResource(value = "checkCurrentUserGoogleCode", description = "校验当前用户的谷歌验证码", authMethod = CoreConstant.AuthMethod.ALLSYSTEMUSER)
  public ResponseResult checkCurrentUserGoogleCode(@PathVariable String mfaValue, HttpServletRequest httpServletRequest) throws Exception {
    ResponseResult responseResult = ResponseResult.createSuccessResult();
    RequestContext currentRequestContext = RequestContextManager.single().getRequestContext();
    LoginUserDetails user = currentRequestContext.getUser();
    String googleSecret = GoogleAuthenticatorUtil.single().getCurrentUserVerifyKey();
    final Boolean isValidatePass = GoogleAuthenticatorUtil.single().checkGoogleVerifyCode(googleSecret, mfaValue);
    redisUtil
        .set(__MFA_TOKEN_USER_CACHE_KEY + user.getId() + ":" + redisUtil.getMd5Key(CommonUtil.single().getIpAddress(httpServletRequest)), isValidatePass,
            expiredTime);

    return responseResult;
  }

}
