package org.cloud.feign.service;

import org.cloud.feign.FeignTracerConfiguration;
import org.cloud.model.TFrameMenu;
import org.cloud.model.TFrameworkResource;
import org.cloud.model.TMicroserviceRegister;
import org.cloud.vo.CommonApiResult;
import org.cloud.vo.FrameUserRefVO;
import org.cloud.vo.MessageLogVO;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "${spring.application.group:}COMMON-SERVICE", contextId = "onCommonService", configuration = {FeignTracerConfiguration.class})  // 不区分大小写
public interface ICommonServiceFeignClient {

    @PostMapping(value = "/inner/userinfo/addUserRef", consumes = MediaType.APPLICATION_JSON_VALUE)
    Integer addUserRef(@RequestBody FrameUserRefVO frameUserRefVO);

    @GetMapping(value = "/inner/userinfo/getCurrentUserRefByAttributeName")
    FrameUserRefVO getCurrentUserRefByAttributeName(@RequestParam("name") String attributeName);

}
