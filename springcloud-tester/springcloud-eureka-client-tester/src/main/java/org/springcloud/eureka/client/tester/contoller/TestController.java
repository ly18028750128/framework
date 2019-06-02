package org.springcloud.eureka.client.tester.contoller;

import java.util.Map;

import org.cloud.core.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/tester",produces="application/vnd.spring-boot.actuator.v1+json;charset=UTF-8")
public class TestController
{
    @Autowired
    RedisUtil redisUtil;
    @RequestMapping(value="/redis/setValue/{key}",method=RequestMethod.POST)
    public Map<String,Object> setValue(@PathVariable("key") String key , @RequestBody Map<String,Object> value){
        redisUtil.set(key, value);
        return (Map<String,Object>)redisUtil.get(key);
    }
}
