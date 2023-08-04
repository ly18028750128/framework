package com.unknow.first.service;

import lombok.extern.slf4j.Slf4j;
import org.cloud.constant.CoreConstant.AuthMethod;
import org.cloud.dimension.annotation.SystemResource;
import org.cloud.vo.CommonApiResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
@Slf4j
@RestController
@RequestMapping("/test")
@RefreshScope
public class TestService {


    @Value("${test.value}")
    private String value;

    @SystemResource(authMethod = AuthMethod.NOAUTH)
    @GetMapping
    public CommonApiResult<String> test(){
        return CommonApiResult.createSuccessResult(value);
    }

}
