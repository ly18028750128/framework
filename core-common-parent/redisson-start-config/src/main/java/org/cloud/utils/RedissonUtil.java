package org.cloud.utils;

import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.util.Assert;

@Slf4j
public final class RedissonUtil {

    private RedissonUtil() {
    }

    private final static RedissonUtil that = new RedissonUtil();

    public static RedissonUtil single() {
        return that;
    }

    @Getter
    private static RedissonClient redissonClient;

    public void initRedissonClient(final RedissonClient redissonClient) {
        Assert.isNull(RedissonUtil.redissonClient, "已经初始化，不能再次初始");
        RedissonUtil.redissonClient = redissonClient;
    }

    /**
     * 获取分布式锁
     *
     * @param name     锁的名称
     * @param waitTime 等待时间
     * @param lockTime 锁定时间
     * @return
     */
    public boolean lock(String name, Long waitTime, Long lockTime) {
        return this.lock(name, waitTime, lockTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 解锁
     *
     * @param name
     * @return
     */
    public void unLock(String name) {
        RLock lock = redissonClient.getLock(name);
        lock.unlock();
    }

    /**
     * 获取锁
     *
     * @param name
     * @param waitTime
     * @param lockTime
     * @param timeUnit
     * @return
     */
    public boolean lock(String name, Long waitTime, Long lockTime, TimeUnit timeUnit) {
        try {
            RLock lock = redissonClient.getLock(name);
            return lock.tryLock(waitTime, lockTime, timeUnit);
        } catch (Exception e) {
            log.error("获取[{}}失败！{}", name, e.getMessage());
            return false;
        }
    }

    public <V> V getValue(final String key) {
        RBucket<V> rBucket = redissonClient.getBucket(key);
        return rBucket.get();
    }

    public void setValue(final String key, final Object Object) {
        RBucket<Object> rBucket = redissonClient.getBucket(key);
        rBucket.set(Object);
    }

    public void remove(final String key) {
        RBucket<Object> rBucket = redissonClient.getBucket(key);
        rBucket.delete();
    }

    public <V> V hashPut(final String name, final Object key, final V value) {
        RMap<Object, V> rBucket = redissonClient.getMap(name);
        return rBucket.put(key, value);
    }

    public <V> Boolean hashFastPut(final String name, final Object key, final V value) {
        RMap<Object, V> rBucket = redissonClient.getMap(name);
        return rBucket.fastPut(key, value);
    }

    public <V> V hashGet(final String name, final Object key) {
        RMap<Object, V> rMap = redissonClient.getMap(name);
        return rMap.get(key);
    }

    public void listAdd(final String name, Object value) {
        RList<Object> rList = redissonClient.getList(name);
        rList.add(value);
    }

    public void listRemove(final String name, Object value) {
        RList<Object> rList = redissonClient.getList(name);
        rList.remove(value);
    }

    public void listRemove(final String name, int index) {
        RList<Object> rList = redissonClient.getList(name);
        rList.remove(index);
    }


}
