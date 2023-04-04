package org.cloud.core.redis;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import org.cloud.utils.CommonUtil;
import org.cloud.utils.MD5Encoder;
import org.cloud.utils.SpringContextUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;


/**
 * 此类将在未来的过程中不再维护，相关功能请用RedissonUtil
 */
@Component
public class RedisUtil {

    @Getter
    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${sys.setting.app.cacheName:}")
    private String cacheName;   // 可以用来区分个个不同的环境，必须全局一致

    private final static RedisUtil redisUtil = new RedisUtil();

    public static RedisUtil single() {
        if (redisUtil.redisTemplate == null) {
            redisUtil.redisTemplate = SpringContextUtil.getBean("redisTemplate");
        }
        if (redisUtil.cacheName == null) {
            redisUtil.cacheName = CommonUtil.single().getEnv("sys.setting.app.cacheName", "");
        }
        return redisUtil;
    }

    /**
     * 批量删除对应的value
     *
     * @param keys 要移除的Key
     */
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(getKey(key));
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
            redisTemplate.delete(getKey(key));
        }
    }

    @NotNull
    public String getKey(String key) {
        return cacheName + key;
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(getKey(key));
    }

    /**
     * 自增长
     *
     * @param key
     * @param data
     * @return
     */
    public Long increment(final String key, int data) {
        return redisTemplate.opsForValue().increment(getKey(key), data);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    public <T> T get(final String key) {
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        return (T) operations.get(getKey(key));
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
        operations.set(getKey(key), value);
        return true;
    }

    /**
     * 写入缓存并设置过期时间（不存在就写入，存在不作操作）
     *
     * @param key
     * @param value
     * @param expireTime
     * @return true成功 false已存在
     */
    @SuppressWarnings("unchecked")
    public boolean setIfAbsent(final String key, Object value, Long expireTime) {
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        return operations.setIfAbsent(getKey(key), value, expireTime, TimeUnit.SECONDS);
    }

    /**
     * 写入缓存（不存在就写入，存在不作操作）
     *
     * @param key
     * @param value
     * @return
     */
    public boolean setIfAbsent(final String key, Object value) {
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        return operations.setIfAbsent(getKey(key), value);
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
        operations.set(key, value);
        if (expireTime > 0) {
            redisTemplate.expire(getKey(key), expireTime, TimeUnit.SECONDS);
        }
        return true;
    }

    public boolean hashSet(final String key, Map<String, Object> value, Long expireTime) {
        HashOperations<String, String, Object> operations = redisTemplate.opsForHash();
        operations.putAll(getKey(key), value);
        if (expireTime > 0) {
            redisTemplate.expire(getKey(key), expireTime, TimeUnit.SECONDS);
        }
        return true;
    }

    public boolean hashSet(final String key, Map<String, Object> value) {
        return hashSet(key, value, -1L);
    }

    public boolean hashSet(final String key, String field, Object value, Long expireTime) {
        HashOperations<String, String, Object> operations = redisTemplate.opsForHash();
        operations.put(getKey(key), field, value);
        if (expireTime > 0) {
            redisTemplate.expire(getKey(key), expireTime, TimeUnit.SECONDS);
        }
        return true;
    }

    public boolean hashSet(final String key, String field, Object value) {
        return hashSet(key, field, value, 0L);
    }

    public Long hashDel(final String key, String... fields) {
        HashOperations<String, String, Object> operations = redisTemplate.opsForHash();
        return operations.delete(getKey(key), fields);
    }

    public <T> T hashGet(final String key, String field) {
        HashOperations<String, String, Object> operations = redisTemplate.opsForHash();
        return (T) operations.get(key, field);
    }

    public <T> Set<T> hashKeys(final String key) {
        HashOperations<String, String, Object> operations = redisTemplate.opsForHash();
        return (Set<T>) operations.keys(key);
    }

    public <K, V> Map<K, V> hashGetEntries(final String key) {
        HashOperations<String, String, Object> operations = redisTemplate.opsForHash();
        return (Map<K, V>) operations.entries(key);
    }

    public <K, V> Map.Entry<K, V> hashGetLastEntry(final String key) {
        HashOperations<String, String, Object> operations = redisTemplate.opsForHash();
        Map<K, V> entries = (Map<K, V>) operations.entries(key);
        return (Map.Entry<K, V>) entries.entrySet().stream().toArray()[entries.entrySet().size() - 1];
    }

    public <K, V> Map.Entry<K, V> hashGetFirstEntry(final String key) {
        HashOperations<String, String, Object> operations = redisTemplate.opsForHash();
        Map<K, V> entries = (Map<K, V>) operations.entries(key);
        return (Map.Entry<K, V>) entries.entrySet().stream().toArray()[0];
    }

    public <V> List<V> hashGet(final String key, Collection<String> fields) {
        HashOperations<String, String, V> operations = redisTemplate.opsForHash();
        return operations.multiGet(key, fields);
    }

    public <V> Long listRightPushAll(final String key, Collection<V> values, Long expireTime) {
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        if (expireTime > 0) {
            redisTemplate.expire(getKey(key), expireTime, TimeUnit.SECONDS);
        }
        return operations.rightPushAll(key, values);
    }

    public <V> Long listRightPushAll(final String key, V[] values, Long expireTime) {
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        if (expireTime > 0) {
            redisTemplate.expire(getKey(key), expireTime, TimeUnit.SECONDS);
        }
        return operations.rightPushAll(key, values);
    }

    public <V> Long listLeftPushAll(final String key, Collection<V> values, Long expireTime) {
        ListOperations<String, V> operations = redisTemplate.opsForList();
        if (expireTime > 0) {
            redisTemplate.expire(getKey(key), expireTime, TimeUnit.SECONDS);
        }
        return operations.leftPushAll(key, values);
    }

    // list将用于一些队列的场景，这里可以不用进行一些操作，这里不用缓存超时时间的设置
    public Long listRightPush(final String key, Object value) {
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        return operations.rightPush(key, value);
    }

    public Long listLeftPush(final String key, Object value) {
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        return operations.leftPush(key, value);
    }

    // list将用于一些队列的场景，这里可以不用进行一些操作，这里不用缓存超时时间的设置
    public <V> V listRightPop(final String key) {
        ListOperations<String, V> operations = redisTemplate.opsForList();
        return operations.rightPop(key);
    }

    public <V> List<V> listRightPopAll(final String key, Long size) {
        List<V> result = new ArrayList<>();
        final String lockKey = key + "_lock";
        final boolean isLock = this.getLock(lockKey, 1000);
        try {
            if (!isLock) {
                return result;
            }
            for (int i = 0; i < size; i++) {
                V value = this.listRightPop(key);
                if (value == null) {
                    continue;
                }
                result.add(value);
            }
        } finally {
            if (isLock) {
                this.releaseLock(lockKey);
            }
        }
        return result;
    }

    public Long listSize(final String key) {
        ListOperations<String, Object> operations = redisTemplate.opsForList();
        return operations.size(key);
    }

    public <V> V listLeftPop(final String key) {
        ListOperations<String, V> operations = redisTemplate.opsForList();
        return operations.leftPop(key);
    }

    public <V> List<V> listLeftPopAll(final String key, Long size) {
        List<V> result = new ArrayList<>();
        final String lockKey = key + "_lock";
        final boolean isLock = this.getLock(lockKey, 1000);
        try {
            if (!isLock) {
                return result;
            }
            for (int i = 0; i < size; i++) {
                V value = this.listLeftPop(key);
                if (value == null) {
                    continue;
                }
                result.add(value);
            }
        } finally {
            if (isLock) {
                this.releaseLock(lockKey);
            }
        }
        return result;
    }

    /**
     * @param key
     * @param timeout 获取数据的根据超时时间
     * @param <V>
     * @return
     */
    public <V> V listRightPop(final String key, Long timeout) {
        ListOperations<String, V> operations = redisTemplate.opsForList();
        return operations.rightPop(key, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * @param key
     * @param timeout 获取数据的根据超时时间
     * @param <V>
     * @return
     */
    public <V> V listLeftPop(final String key, Long timeout) {
        ListOperations<String, V> operations = redisTemplate.opsForList();

        return operations.leftPop(key, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 根据列表起始结束为止获取数据
     *
     * @param key
     * @param start
     * @param end
     * @param <V>
     * @return
     */
    public <V> List<V> listRange(final String key, int start, int end) {
        ListOperations<String, V> operations = redisTemplate.opsForList();
        List<V> list = operations.range(key, start, end);
        return list;
    }

    /**
     * 根据索引获取集合中的元素
     *
     * @param key
     * @param index
     * @param <V>
     * @return
     */
    public <V> V listIndex(final String key, int index) {
        ListOperations<String, V> operations = redisTemplate.opsForList();
        V value = operations.index(key, index);
        return value;
    }


    /**
     * 向集合中指定索引下添加一个新元素，并覆盖当前集合中指定位置的值
     *
     * @param key
     * @param index
     * @param value
     * @param <V>
     */
    public <V> void listSet(final String key, int index, V value) {
        ListOperations<String, V> operations = redisTemplate.opsForList();
        operations.set(key, index, value);
    }


    private final String locker_prefix_name = "system:locker:";

    /**
     * 获得锁
     */
    public boolean getLock(String lockId, long millisecond) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(locker_prefix_name + lockId, "lock", millisecond, TimeUnit.MILLISECONDS);
        return success != null && success;
    }

    /**
     * 释放锁
     *
     * @param lockId
     */
    public void releaseLock(String lockId) {
        redisTemplate.delete(locker_prefix_name + lockId);
    }

    public String getMd5Key(String key) {
        return MD5Encoder.encode(key);
    }

}
