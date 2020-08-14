package org.cloud.utils;


import com.longyou.comm.starter.CommonServiceApplication;
import lombok.extern.slf4j.Slf4j;
import org.cloud.core.redis.RedisUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CommonServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RedisUtilTest {
    @Autowired
    RedisUtil redisUtil;

    @Test
    public void testHashGetLastEntry(){
        final String hashKey = "test1111111";

        redisUtil.hashSet(hashKey,"1","1",1000000L);
        redisUtil.hashSet(hashKey,"2","2",1000000L);
        redisUtil.hashSet(hashKey,"3","3",1000000L);

        Assert.assertEquals(redisUtil.hashGetFirstEntry(hashKey).getValue(),"1");
        Assert.assertEquals(redisUtil.hashGetLastEntry(hashKey).getValue(),"3");

//        redisUtil.hashDel()

    }
}
