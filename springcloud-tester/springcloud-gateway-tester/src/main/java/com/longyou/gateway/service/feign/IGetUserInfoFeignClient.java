package com.longyou.gateway.service.feign;

import org.cloud.entity.LoginUserDetails;
import org.cloud.vo.LoginUserGetParamsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("${spring.application.group:}COMMON-SERVICE")  // 不区分大小写
public interface IGetUserInfoFeignClient {
    @PostMapping(value = "/userinfo/getUserByName", consumes = MediaType.APPLICATION_JSON_VALUE)
    LoginUserDetails getUserInfo(@RequestBody LoginUserGetParamsDTO loginUserGetParamsDTO);
}

