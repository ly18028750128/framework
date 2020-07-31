package org.cloud.utils;


import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public final class LoginUtil {
    private LoginUtil() {
    }

    private static LoginUtil instance = new LoginUtil();

    public static LoginUtil single() {
        return instance;
    }

    public Map<String, Object> login(MultiValueMap<String, String> params) {
        RestTemplate restTemplate = SpringContextUtil.getBean(RestTemplate.class);
        final String applicationGroup = CommonUtil.single().getEnv("spring.application.group", "");
        String url = "http://" + applicationGroup + "SPRING-GATEWAY/auth/login";
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); //  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers); //  封装参数，千万不要替换为Map与HashMap，否则参数无法传递

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);//  执行HTTP请求

        return response.getBody();
    }


}
