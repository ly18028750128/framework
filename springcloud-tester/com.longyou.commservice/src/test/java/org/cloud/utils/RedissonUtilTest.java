package org.cloud.utils;

import com.longyou.comm.starter.CommonServiceApplication;
import lombok.extern.slf4j.Slf4j;
import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.process.ProcessUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBlockingQueue;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CommonServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class RedissonUtilTest {

    @Test
    void getValue() throws Exception {
        LoginUserDetails result = RedissonUtil.single().getValue("USER:LOGIN:SUCCESS:CACHE:USER:1d19decd5105678b72d8ad3fb3e58385");
        Assert.isTrue(result != null, "获取失败");
        RedissonUtil.single().setValue("USER:LOGIN:SUCCESS:CACHE:USER:junitTest", result);
        List<LoginUserDetails> loginUserDetails = new ArrayList<>();
        loginUserDetails.add(result);
        loginUserDetails.add(new LoginUserDetails());
        loginUserDetails.add(new LoginUserDetails());
        RedissonUtil.single().setValue("USER:LOGIN:SUCCESS:CACHE:USER:junitListTest", loginUserDetails);
        List<LoginUserDetails> loginUserDetailsBack = RedissonUtil.single().getValue("USER:LOGIN:SUCCESS:CACHE:USER:junitListTest");
        Assert.notEmpty(loginUserDetailsBack, "不能为空！");
    }

    @Test
    void lockAndUnlock() {
        List<Callable<Boolean>> callables = new ArrayList<>();
        final String lockName = "lock.junit.test1";
        for (int i = 0; i < 100; i++) {
            callables.add(() -> {
                Boolean result = RedissonUtil.single().lock(lockName, 5L, 1000L);
                try {
                    log.info(Thread.currentThread().getName() + "." + lockName + "=" + result);
                    Thread.sleep(100L);
                } finally {
                    if (result) {
                        RedissonUtil.single().unLock(lockName);
                    }
                }
                return true;
            });
        }

        ProcessUtil.single().runCablles(callables, 10, 120L);
    }

    @Test
    void hGetAndHSet() {
        final String hashName = "junit:hash:test";
        LoginUserDetails userDetails = new LoginUserDetails();
        userDetails.setId(101L);
        LoginUserDetails userDetailsBack1 = RedissonUtil.single().hashPut(hashName, userDetails.getId(), userDetails);
        Assert.isTrue(userDetails.getId() == userDetailsBack1.getId(), "获取值不相等！");
        LoginUserDetails userDetailsBack = RedissonUtil.single().hashGet(hashName, userDetails.getId());
        Assert.isTrue(userDetails.getId() == userDetailsBack.getId(), "获取值不相等！");
        userDetails.setId(102L);
        RedissonUtil.single().hashFastPut(hashName, userDetails.getId(), userDetails);
    }

    @Test
    void blockingQueueTest() {
        final String blockingQueueName = "junit:hash:test";
        RBlockingQueue<Integer> rBlockingQueue = RedissonUtil.getRedissonClient().getBlockingQueue(blockingQueueName);

        
    }

}