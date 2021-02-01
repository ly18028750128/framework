package org.cloud.controller;

import javax.annotation.PostConstruct;
import org.cloud.annotation.SystemResource;
import org.cloud.common.service.AESService;
import org.cloud.constant.CoreConstant.AuthMethod;
import org.cloud.utils.SpringContextUtil;
import org.cloud.vo.ResponseResult;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Lazy
@RestController
@RequestMapping("/common/aes")
@SystemResource(path = "common/aes")
public class AESController {

  AESService aesService;

  @PostConstruct
  void setAesService() {
    aesService = SpringContextUtil.getBean(AESService.class);
  }

  /**
   * 加密
   *
   * @param str
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/encrypt", method = RequestMethod.POST)
  public ResponseResult encrypt(@RequestBody String str) throws Exception {
    Assert.notNull(aesService, "aes.disabled"); // 未开放aes加密
    ResponseResult result = ResponseResult.createSuccessResult();
    result.addData(aesService.encrypt(str));
    return result;
  }

  /**
   * 解密
   *
   * @param encryptStr
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/decrypt", method = RequestMethod.POST)
  @SystemResource(value = "decrypt",description = "des加密", authMethod = AuthMethod.BYUSERPERMISSION)
  public ResponseResult decrypt(@RequestBody String encryptStr) throws Exception {
    Assert.notNull(aesService, "aes.disabled"); // 未开放aes加密
    ResponseResult result = ResponseResult.createSuccessResult();
    result.addData(aesService.decrypt(encryptStr));
    return result;
  }
}
