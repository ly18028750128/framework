package org.cloud.feign.service;

import org.cloud.feign.FeignTracerConfiguration;
import org.cloud.model.TFrameMenu;
import org.cloud.model.TFrameworkResource;
import org.cloud.model.TMicroserviceRegister;
import org.cloud.vo.FrameUserRefVO;
import org.cloud.vo.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "${spring.application.group:}COMMON-SERVICE", contextId = "onCommonService", configuration = {FeignTracerConfiguration.class})  // 不区分大小写
public interface ICommonServiceFeignClient {

    @PostMapping(value = "/inner/userinfo/addUserRef", consumes = MediaType.APPLICATION_JSON_VALUE)
    Integer addUserRef(@RequestBody FrameUserRefVO frameUserRefVO);

    @GetMapping(value = "/inner/userinfo/getCurrentUserRefByAttributeName")
    FrameUserRefVO getCurrentUserRefByAttributeName(@RequestParam("name") String attributeName);


    @PostMapping(value = "/inner/system/resource/register/microservice", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseResult<Integer> saveOrUpdateMicroserviceRegister(@RequestBody TMicroserviceRegister microserviceRegister);

    @PostMapping(value = "/inner/system/resource/register/menu", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseResult<TFrameMenu> saveOrUpdateMenu(@RequestBody TFrameMenu frameMenu);

    @PostMapping(value = "/inner/system/resource/register/resource", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseResult<TFrameworkResource> saveOrUpdateResource(@RequestBody TFrameworkResource frameworkResource);


}
