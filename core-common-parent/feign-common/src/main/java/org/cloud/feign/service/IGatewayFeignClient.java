package org.cloud.feign.service;

import org.cloud.entity.LoginUserDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Lazy
@FeignClient("${spring.application.group:}SPRING-GATEWAY")  // 不区分大小写
public interface IGatewayFeignClient {

    @GetMapping(value = "/user/info/authentication")
    LoginUserDetails getAuthentication();

    /**
     * 根据token获取用户信息
     *
     * @param token
     * @return
     */
    @GetMapping(value = "/user/info/authentication")
    LoginUserDetails getAuthentication(@RequestHeader("Authorization") String token);

}
