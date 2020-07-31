package com.longyou.comm.conntroller;

import lombok.extern.slf4j.Slf4j;
import org.cloud.entity.LoginUserDetails;
import org.cloud.feign.service.IGatewayFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feign/test")
@RefreshScope
@Slf4j
public class FeignTestController {
    @Autowired
    IGatewayFeignClient gatewayFeignClient;

    @Value("${system.test.value:测试值}")
    private String test;

    @GetMapping("/gateway/user/info/authentication")
    public Object getAuthentication() {
        LoginUserDetails userDetails = gatewayFeignClient.getAuthentication();
        log.info("system.test.value={}", test);
        return userDetails;
    }

}
