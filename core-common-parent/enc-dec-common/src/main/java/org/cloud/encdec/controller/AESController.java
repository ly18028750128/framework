package org.cloud.encdec.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.annotation.PostConstruct;
import org.cloud.annotation.SystemResource;
import org.cloud.encdec.service.AESService;
import org.cloud.constant.CoreConstant.AuthMethod;
import org.cloud.utils.SpringContextUtil;
import org.cloud.vo.ResponseResult;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "AESController", tags = "AES加解密")
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
     * 加密,不需要权限
     *
     * @param str 需要加密的字符串
     * @return 加密后的字符
     * @throws Exception 异常
     */
    @ApiOperation(value = "encrypt", notes = "AES加密")
    @RequestMapping(value = "/encrypt", method = RequestMethod.POST)
    public ResponseResult<String> encrypt(@RequestBody String str) throws Exception {
        Assert.notNull(aesService, "system.warn.aes.disabled"); // 未开放aes加解密
        return ResponseResult.createSuccessResult(aesService.encrypt(str));
    }

    /**
     * 解密
     *
     * @param encryptStr
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "decrypt", notes = "AES解密")
    @RequestMapping(value = "/decrypt", method = RequestMethod.POST)
    @SystemResource(value = "decrypt", description = "AES解密", authMethod = AuthMethod.BYUSERPERMISSION)
    public ResponseResult<String> decrypt(@RequestBody String encryptStr) throws Exception {
        Assert.notNull(aesService, "system.warn.aes.disabled"); // 未开放aes加解密
        return ResponseResult.createSuccessResult(aesService.decrypt(encryptStr));
    }
}
