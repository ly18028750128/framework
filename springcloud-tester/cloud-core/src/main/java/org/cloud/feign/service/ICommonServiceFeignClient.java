package org.cloud.feign.service;

import org.cloud.vo.FrameUserRefVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${spring.application.group:}COMMON-SERVICE", contextId = "onCoreProject")  // 不区分大小写
public interface ICommonServiceFeignClient {
    @PostMapping(value = "/inner/userinfo/addUserRef", consumes = MediaType.APPLICATION_JSON_VALUE)
    Integer addUserRef(@RequestBody FrameUserRefVO frameUserRefVO);
}
