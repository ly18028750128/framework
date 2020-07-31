package org.cloud.utils;

import com.longyou.comm.starter.CommonServiceApplication;
import lombok.extern.slf4j.Slf4j;
import org.cloud.feign.service.IGatewayFeignClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CommonServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RsaUtilTest {

    @Autowired
    IGatewayFeignClient gatewayFeignClient;

    @Autowired
    RestTemplate restTemplate;

    @Test
    public void encryptByRedisRsaKey() throws Exception {
//        final String testStr = "abc123";
//
//        final String encryptStr = RsaUtil.single().encryptByRedisRsaKey(testStr);
//
//        log.info("encryptStr=" + encryptStr);
//
//        final String decryptStr = RsaUtil.single().decryptByRedisRsaKey(encryptStr);
//
//        log.info("decryptStr=" + decryptStr);
//        Assert.assertTrue(testStr.equals(decryptStr));

//        Map<String, Object> params = new HashMap<>();
//
//        params.put("username","admin");
//        params.put("password","123456");
//        gatewayFeignClient.login(params);


        String url = "http://LONGYOUSPRING-GATEWAY/auth/login";
////        RestTemplate client = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
//  请勿轻易改变此提交方式，大部分的情况下，提交方式都是表单提交
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//  封装参数，千万不要替换为Map与HashMap，否则参数无法传递
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
////  也支持中文
        params.add("username", RsaUtil.single().encryptByRedisRsaKey("unknowaccount111"));
        params.add("password", String.valueOf(Math.random() + System.currentTimeMillis()));
        params.add("microServiceName", "unknowaccount");
//        params.add("password", "123456");
        params.add("loginType", "LOGIN-BY-THIRD-LOGIN");

        Map<String, Object> result = LoginUtil.single().login(params);


        log.info("{}", result);


//        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(params, headers);
////  执行HTTP请求
//        ResponseEntity<LoginUserDetails> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, LoginUserDetails.class);
//
////        response = null;
//        Map<String, String> params = new LinkedHashMap<>();
//        params.put("username", RsaUtil.single().encryptByRedisRsaKey("admin"));
////        params.put("password", "123456");
//        params.put("loginType", "LOGIN-BY-THIRD-LOGIN");
//        LoginUserDetails loginUserDetails =
//                gatewayFeignClient.login(params);
//
//        loginUserDetails = null;

    }

}
