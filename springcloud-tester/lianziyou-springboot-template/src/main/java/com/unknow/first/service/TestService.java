package com.unknow.first.service;

import static com.unknow.first.api.encrypt.ApiEncryptType.BY_USER;

import com.unknow.first.api.encrypt.annotation.ApiEncrypt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.cloud.constant.CoreConstant.AuthMethod;
import org.cloud.dimension.annotation.SystemResource;
import org.cloud.vo.CommonApiResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
@RefreshScope
@Api(value = "测试", tags = "测试")
public class TestService {


    @Value("${test.value}")
    private String value;

    @SystemResource(authMethod = AuthMethod.NOAUTH)
    @GetMapping("/refresh/value")
    public CommonApiResult<String> test() {
        return CommonApiResult.createSuccessResult(value);
    }

    @GetMapping("/api/encrypt/system")
    @ApiOperation("api加密-系统密钥加密")
    @ApiEncrypt
    public CommonApiResult<Object> testEncryptBySystemKey() {
        return CommonApiResult.createSuccessResult("加密测试");
    }

    @GetMapping("/api/encrypt/user")
    @ApiOperation("api加密-用户密钥加密")
    @ApiEncrypt(encryptType = BY_USER)
    @SystemResource(authMethod = AuthMethod.ALLSYSTEMUSER)
    public CommonApiResult<Object> testEncryptByUserKey() {

        Map<String, Object> value = new HashMap<>();

        value.put("test1", "测试1");
        value.put("test2", "测试2");

        return CommonApiResult.createSuccessResult(value);
    }

}
