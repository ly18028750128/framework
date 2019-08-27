package org.springcloud.eureka.client.tester.contoller;

import com.alibaba.fastjson.JSONObject;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.AuthUserDetails;
import org.cloud.utils.RestTemplateUtil;
import org.cloud.utils.process.ProcessCallable;
import org.cloud.utils.process.ProcessUtil;
import org.cloud.vo.JavaBeanResultMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springcloud.eureka.client.tester.dao.IUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import org.springframework.security.core.userdetails.User;

@RestController
//@RequestMapping(value = "/tester", produces = "application/vnd.spring-boot.actuator.v1+json;charset=UTF-8")
@RequestMapping(value = "/tester")
public class TestController {
    Logger logger = LoggerFactory.getLogger(TestController.class);
    @Autowired
    RedisUtil redisUtil;

    @RequestMapping(value = "/redis/setValue/{key}", method = RequestMethod.POST)
    public Map<String, Object> setValue(@PathVariable("key") String key, @RequestBody Map<String, Object> value) {
        redisUtil.set(key, value);
        return (Map<String, Object>) redisUtil.get(key);
    }

    @Autowired
    IUserDao userDao;

    @Autowired
    RestTemplate restTemplate;

    @RequestMapping(value = "/user/get/{userName}", method = RequestMethod.GET)
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Map<String, Object> getByUserName(@PathVariable("userName") String userName, HttpServletRequest request) {

        HttpHeaders headers = RestTemplateUtil.single().getHttpHeadersFromHttpRequest(request);
        AuthUserDetails user = RestTemplateUtil.single().execute("http://SPRING-GATEWAY/user/info/authentication", HttpMethod.GET, null, headers, AuthUserDetails.class);
        JavaBeanResultMap<Object> result = userDao.getUserByName(userName);
        return result;
    }

    @RequestMapping(value = "/testProcess/{threedSize}/{poolSize}", method = RequestMethod.GET)
    public List<?> testProcess(@PathVariable("threedSize") int threedSize, @PathVariable("poolSize") int poolSize) {
        List<Runnable> runnables = new ArrayList<>();
        for (int i = 0; i < threedSize; i++) {
            final int j = i;
            runnables.add(new ProcessCallable<Object>() {
                @Override
                public Object process() {
                    logger.info(j + "号进程(exceute)" + System.currentTimeMillis());
                    return j;
                }
            });
        }
        ProcessUtil.single().submitCablles(runnables);

        List<Callable<Object>> callables = new ArrayList<>();
        for (int i = 0; i < threedSize; i++) {
            final int j = i;
            callables.add(new ProcessCallable<Object>() {
                @Override
                public Object process() {
                    try {
                        Thread.sleep(10);  //这里要给一定的执行时间，不然测试看不出效果
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    logger.info(j + "号进程(submit)" + System.currentTimeMillis());
                    return j;
                }
            });
        }
        return ProcessUtil.single().runCablles(callables, poolSize, 10000L);
    }

}
