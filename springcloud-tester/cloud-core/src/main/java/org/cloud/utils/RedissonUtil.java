package org.cloud.utils;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.K;
import org.jetbrains.annotations.NotNull;
import org.redisson.api.*;
import org.springframework.util.Assert;

import java.util.concurrent.TimeUnit;

@Slf4j
public final class RedissonUtil {

    private RedissonUtil() {
    }

    private final static RedissonUtil that = new RedissonUtil();

    @Getter
    private RedissonClient redissonClient;

    public void initRedissonClient(@NotNull final RedissonClient redissonClient) {
        Assert.isNull(this.redissonClient, "已经初始化，不能再次初始");
        this.redissonClient = redissonClient;
    }

    public static RedissonUtil single() {
        return that;
    }

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
            log.error("获取[" + name + "]失败！");
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
}
