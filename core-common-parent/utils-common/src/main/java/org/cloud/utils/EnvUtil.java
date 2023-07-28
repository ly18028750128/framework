package org.cloud.utils;


import cn.hutool.core.bean.BeanUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;

public final class EnvUtil {
    final Environment env;
    @Lazy
    private EnvUtil() {
        env = SpringContextUtil.getBean(Environment.class);
    }

    private static class Handler {

        private Handler() {
        }

        private static EnvUtil handler = new EnvUtil();
    }

    public static EnvUtil single() {
        return Handler.handler;
    }

    /**
     * 获取当前配置参数的值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getEnv(String key, String defaultValue) {
        return env.getProperty(key, defaultValue);
    }

    public String getEnv(String key) {
        return env.getProperty(key);
    }

    public <T> T getEnv(String key, T defaultValue, Class cls) {
        final String value = getEnv(key);
        if (value == null) {
            return defaultValue;
        }

        try {
            return (T) cls.getConstructor(String.class).newInstance(value);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getEnv(String key, Class cls) {
        return getEnv(key, null, cls);
    }

}
