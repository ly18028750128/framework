package org.cloud.feign.service;

import org.cloud.entity.LoginUserDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("${spring.application.group:}SPRING-GATEWAY")  // 不区分大小写
public interface IGatewayFeignClient {
    @GetMapping(value = "/user/info/authentication")
    LoginUserDetails getAuthentication();
}
