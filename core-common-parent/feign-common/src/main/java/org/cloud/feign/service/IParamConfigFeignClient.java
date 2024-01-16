package org.cloud.feign.service;

import org.cloud.feign.config.FeignTracerConfiguration;
import org.cloud.vo.ParamConfigVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Lazy
@FeignClient(name = "${spring.application.group:}COMMON-SERVICE", contextId = "onCommonServiceParamConfig", configuration = {FeignTracerConfiguration.class})
// 不区分大小写
public interface IParamConfigFeignClient {

    @GetMapping("/inner/param/config/get/{code}")
    ParamConfigVO get(@PathVariable("code") String code);

    @PostMapping("/inner//param/config/update")
    Boolean update(@RequestBody ParamConfigVO paramConfigVO);

}
