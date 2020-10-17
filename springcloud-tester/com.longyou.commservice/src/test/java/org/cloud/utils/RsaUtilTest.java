package org.cloud.utils;

import com.alibaba.fastjson.JSON;
import com.longyou.comm.starter.CommonServiceApplication;
import lombok.extern.slf4j.Slf4j;
import org.cloud.entity.LoginUserDetails;
import org.cloud.feign.service.IGatewayFeignClient;
import org.cloud.utils.process.ProcessUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

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
        List<Callable<Boolean>> callables = new ArrayList<Callable<Boolean>>();
        for (int i = 0; i < 5; i++) {
            final int j = i;
            callables.add(() -> {
                MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
                params.add("username", RsaUtil.single().encryptByRedisRsaKey("unknowaccount"+j));
                params.add("password", String.valueOf(Math.random() + System.currentTimeMillis()));
                params.add("microServiceName", "unknowaccount");
                params.add("loginType", "LOGIN-BY-THIRD-LOGIN");
                Map<String, Object> result = LoginUtil.single().login(params);
                log.info("{}", result);
                return true;
            });
        }
        ProcessUtil.single().runCablles(callables);
    }

    @Test
    public void getUserByToken() throws Exception {
        List<Callable<Boolean>> callables = new ArrayList<Callable<Boolean>>();
        final String token = "YWRtaW46MTg1YjgzZmE5MjdiYzhkMjRhMDg0MGRiMjMzZmVlZWUlYTFiMmMwazNkNHk4JWM2MWM2MTNlNjFhN2QxN2E1ZDUzNDgxNWVlMzdmNDZk";
        for (int i = 0; i < 5; i++) {
            final int j = i;
            callables.add(() -> {
                LoginUserDetails loginUserDetails = LoginUtil.single().getUserByToken(token);
                log.info("{}", JSON.toJSONString(loginUserDetails));
                return true;
            });
        }
        ProcessUtil.single().runCablles(callables);
    }

}
