package org.cloud.feign.service;

import org.cloud.feign.FeignTracerConfiguration;
import org.cloud.vo.CommonApiResult;
import org.cloud.vo.MessageLogVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "${spring.application.group:}COMMON-SERVICE", contextId = "onCommonServiceMessageLog", configuration = {FeignTracerConfiguration.class})
// 不区分大小写
public interface ICommonServiceMessageLogFeign {

    @PostMapping(value = "/inner/message/log/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    Boolean saveMessageLogs(@RequestBody MessageLogVO messageLogVO);
}
