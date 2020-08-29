package org.cloud.utils;


import com.longyou.comm.starter.CommonServiceApplication;
import lombok.extern.slf4j.Slf4j;
import org.cloud.core.redis.RedisUtil;
import org.cloud.utils.process.ProcessUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CommonServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RedisUtilTest {
    @Autowired
    RedisUtil redisUtil;

    @Test
    public void testHashGetLastEntry() {
        final String hashKey = "test1111111";

        redisUtil.hashSet(hashKey, "1", "1", 1000000L);
        redisUtil.hashSet(hashKey, "2", "2", 1000000L);
        redisUtil.hashSet(hashKey, "3", "3", 1000000L);

        Assert.assertEquals(redisUtil.hashGetFirstEntry(hashKey).getValue(), "1");
        Assert.assertEquals(redisUtil.hashGetLastEntry(hashKey).getValue(), "3");

//        redisUtil.hashDel()

    }

    @Test
    public void testRange() throws Exception {
        final String hashKey = "testListRange";
        List<Object> integers = new ArrayList<>();

        integers.add(1);
        integers.add(2);
        integers.add(3);
        integers.add(4);
        log.info("pushSize = {}",redisUtil.listLeftPushAll(hashKey,integers,500L));

        List<Integer> result = redisUtil.listRightPopAll(hashKey,200L);

        log.info("{},{}",result,redisUtil.listSize(hashKey));

//        redisUtil.hashDel()

    }

    @Test
    public void getLock() {
        List<Callable<Boolean>> callables = new ArrayList<Callable<Boolean>>();

        final String lockId = "test1";
        for (int i = 0; i < 10; i++) {
            final int j = i;
            callables.add(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    boolean isLock = redisUtil.getLock(lockId, 2);
                    Thread.sleep(3 + j);
                    try {
                        if (isLock) {
                            log.info(Thread.currentThread().getName() + ",得到锁！");
                            Thread.sleep(Double.valueOf(Math.random() * 3).longValue());
                        } else {
                            log.info(Thread.currentThread().getName() + ",未得到锁！");
                        }

                    } finally {
                        if (isLock) {
                            redisUtil.releaseLock(lockId);
                        }
                    }

                    return true;
                }
            });
        }
        ProcessUtil.single().runCablles(callables);

    }
}
