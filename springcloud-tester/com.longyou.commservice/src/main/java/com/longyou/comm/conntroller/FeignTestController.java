package com.longyou.comm.conntroller;

import org.cloud.annotation.SystemResource;
import org.cloud.entity.LoginUserDetails;
import org.cloud.feign.service.IGatewayFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feign/test")
public class FeignTestController {
    @Autowired
    IGatewayFeignClient gatewayFeignClient;

    @GetMapping("/gateway/user/info/authentication")
    public Object getAuthentication(){
        LoginUserDetails userDetails = gatewayFeignClient.getAuthentication();
        return userDetails;
    }

}
