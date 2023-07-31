package org.cloud.feign.service;

import org.cloud.feign.config.FeignTracerConfiguration;
import org.cloud.vo.FrameUserRefVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
@Lazy
@FeignClient(name = "${spring.application.group:}COMMON-SERVICE", contextId = "onCommonService", configuration = {FeignTracerConfiguration.class})  // 不区分大小写
public interface ICommonServiceFeignClient {

    @PostMapping(value = "/inner/userinfo/addUserRef", consumes = MediaType.APPLICATION_JSON_VALUE)
    Integer addUserRef(@RequestBody FrameUserRefVO frameUserRefVO);

    @GetMapping(value = "/inner/userinfo/getCurrentUserRefByAttributeName")
    FrameUserRefVO getCurrentUserRefByAttributeName(@RequestParam("name") String attributeName);

}
