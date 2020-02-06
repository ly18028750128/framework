package org.cloud.core.redis;

import com.alibaba.fastjson.JSON;
import com.google.common.math.DoubleMath;
import com.longyou.comm.starter.CommonServiceApplication;
import org.cloud.utils.process.ProcessCallable;
import org.cloud.utils.process.ProcessUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CommonServiceApplication.class}, properties = "classpath:testYml.yml")
//@ActiveProfiles(value = "${spring.profiles.active:dev}")
public class RedisUtilTest {

    Logger logger = LoggerFactory.getLogger(RedisUtilTest.class);

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${spring.profiles.active:}")
    private String activeProfile;

    @Test
    public void set() {
        redisUtil.set("junit:1", "单元测试1！");
    }

    @Test
    public void get() {
        redisUtil.get("junit:1");
    }

    @Test
    public void listRightPushAll() {
        final String key = "junit:List:test2";
        redisUtil.remove(key);
        List<Object> values = new ArrayList<>();
        values.add("1");
        values.add("2");
        values.add("3");
        redisUtil.listRightPushAll(key, values, 0L);
        Assert.assertEquals(redisUtil.listLeftPop(key), "1");
        Assert.assertEquals(redisUtil.listRightPop(key), "3");
        ListOperations<String, Object> listOperations = redisTemplate.opsForList();
        Assert.assertEquals(listOperations.size(key), Long.valueOf(1L));
    }

    // redis事物测试
    @Test
    public void testTransaction() {
        final String key = "junit:List:transaction";
        redisUtil.remove(key);
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                redisOperations.multi();
                ListOperations<String, Object> listOperations = redisOperations.opsForList();
                listOperations.rightPush(key, "1");
                listOperations.rightPush(key, "2");
                listOperations.rightPush(key, "3");
                listOperations.rightPush(key, "4");
                listOperations.leftPush(key, "0");
                return redisOperations.exec();
            }
        });
        Assert.assertEquals(redisUtil.listRightPop(key, 1000L), "4");
        Assert.assertEquals(redisUtil.listRightPop(key, 1000L), "3");

        List<Runnable> callables = new ArrayList<>();
        callables.add(new ProcessCallable<Boolean>() {
            @Override
            public Boolean process() {
                logger.error("thread1 = " + redisUtil.listRightPop(key, 1L));
                return true;
            }
        });


        callables.add(new ProcessCallable<Boolean>() {
            @Override
            public Boolean process() {
                logger.error("thread2 = " + redisUtil.listRightPop(key, 1L));
                return true;
            }
        });

        // 这两个线程只会弹出一个数据，因为同时执行时会报错。
        ProcessUtil.single().submitCablles(callables);

    }

    @Test
    public void redisSetTest() {
        final String key = "junit:set:test1";
        redisUtil.remove(key);
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                SetOperations<String, Object> setOperations = redisOperations.opsForSet();
                setOperations.add(key, "a", 1, 2, 3);
                Assert.assertEquals(setOperations.isMember(key, 1), true);
                Assert.assertEquals(setOperations.size(key).longValue(), 4L);
                Assert.assertEquals(setOperations.remove(key, 1).longValue(), 1L);
                Assert.assertEquals(setOperations.remove(key, "b").longValue(), 0L);
                Assert.assertEquals(setOperations.remove(key, "a").longValue(), 1L);
                Assert.assertEquals(setOperations.size(key).longValue(), 2L);
                redisOperations.dump(key);
                return null;
            }
        });
    }

    @Test
    public void TestHyperLogLog() {
        final String key = "junit:hyperLogLog:test1";
        redisUtil.remove(key);
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                HyperLogLogOperations<String, Object> hyperLogLogOperations = redisOperations.opsForHyperLogLog();
                hyperLogLogOperations.add(key, 1, 2, 3, "f");
                return null;
            }
        });
    }

    @Test
    public void TestZset() {
        final String key = "junit:zset:Test1";
        redisUtil.remove(key);
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                ZSetOperations<String, Object> zSetOperations = redisOperations.opsForZSet();
                Set<ZSetOperations.TypedTuple<Object>> sets = new HashSet<>();

                sets.add(new DefaultTypedTuple("a", 1D));
                sets.add(new DefaultTypedTuple(5, 2.0D));
                sets.add(new DefaultTypedTuple("b", 6.0D));
                sets.add(new DefaultTypedTuple("y", 6.0D));

                Assert.assertEquals(zSetOperations.add(key, sets).longValue(), 4L);

                zSetOperations.add(key, "323", 1D);

                Assert.assertEquals(zSetOperations.size(key).longValue(), 5L);
                Set<Object> rangeSet = zSetOperations.range(key, 1L, 2L);
                logger.info(JSON.toJSONString(rangeSet));
                Assert.assertEquals(rangeSet.contains(5), true);
                return null;
            }
        });
        Assert.assertEquals(activeProfile, "dev");
    }

    @Test
    public void testIncr() {

        final int max = 10;

        final String key = "junit:hash:numberHash";

        Object value = redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations redisOperations) throws DataAccessException {
                HashOperations hashOperations = redisOperations.opsForHash();
                hashOperations.put(key, "牛肉礼包", 0);
                List<Runnable> runnables = new ArrayList<>();
                for (int i = 0; i < 15; i++) {
                    final int j = i;
                    runnables.add(new ProcessCallable<Boolean>() {
                        @Override
                        public Boolean process() {
                            Long total = hashOperations.increment(key, "牛肉礼包", 1);
                            if (total > max) {
                                logger.error("thread " + j + ":超出"+max+"个了，不能进行抢购了！");
                            }else{
                                logger.error("thread " + j + ":抢购成功！");
                            }
                            return null;
                        }
                    });
                }
                ProcessUtil.single().submitCablles(runnables);
                return 1L;
            }
        });
        logger.error("junit:hash:numberHash=" + JSON.toJSONString(value));
    }

}