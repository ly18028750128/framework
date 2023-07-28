package org.cloud.utils.math;

import org.springframework.util.Assert;

public final class RandomUtils {
    private RandomUtils() {
    }

    private final static RandomUtils RANDOM_UTILS = new RandomUtils();

    public static RandomUtils getInstance() {
        return RANDOM_UTILS;
    }

    public int getInt(int min, int max) {
        Assert.isTrue(max > min, "请输入正确的参数");
        return Double.valueOf(min + Math.random() * (max - min + 1)).intValue();
    }

}
