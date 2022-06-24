package org.cloud.feign.service;

import org.cloud.feign.FeignTracerConfiguration;
import org.cloud.vo.FrameUserRefVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "${spring.application.group:}COMMON-SERVICE", contextId = "onCommonService", configuration = {FeignTracerConfiguration.class})  // 不区分大小写
public interface ICommonServiceFeignClient {

    @PostMapping(value = "/inner/userinfo/addUserRef", consumes = MediaType.APPLICATION_JSON_VALUE)
    Integer addUserRef(@RequestBody FrameUserRefVO frameUserRefVO);

    @GetMapping(value = "/inner/userinfo/getCurrentUserRefByAttributeName")
    FrameUserRefVO getCurrentUserRefByAttributeName(@RequestParam("name") String attributeName);

}
