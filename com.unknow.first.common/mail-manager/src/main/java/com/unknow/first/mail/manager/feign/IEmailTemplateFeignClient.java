package com.unknow.first.mail.manager.feign;

import com.unknow.first.mail.manager.domain.EmailTemplate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${spring.application.group:}COMMON-SERVICE", contextId = "onEmailManager")  // 不区分大小写
public interface IEmailTemplateFeignClient {

    @GetMapping(value = "/inner/email/getEmailTemplateByCode")
    EmailTemplate getEmailTemplateByCode(@RequestParam("templateCode") String templateCode,
        @RequestParam(value = "language", defaultValue = "zh_CN") String language);


}