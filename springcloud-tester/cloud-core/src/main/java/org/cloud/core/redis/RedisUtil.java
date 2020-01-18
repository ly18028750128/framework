package org.cloud.core.redis;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${sys.setting.app.cacheName:}")
    private String cacheName;   //用来区分各个服务

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(cacheName + key);
        }
    }

    /**
     * 批量删除key,这里暂时先不改
     *
     * @param pattern
     */
    public void removePattern(final String pattern) {
        Set<Serializable> keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    @SuppressWarnings("unchecked")
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(cacheName + key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(cacheName + key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public <T> T get(final String key) {
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        return (T) operations.get(cacheName + key);
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
    public boolean set(final String key, Object value) {
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        operations.set(cacheName + key, value);
        return true;
    }

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public boolean set(final String key, Object value, Long expireTime) {
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        operations.set(cacheName + key, value);
        redisTemplate.expire(cacheName + key, expireTime, TimeUnit.SECONDS);
        return true;
    }

}
