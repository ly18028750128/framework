package org.cloud.utils.math;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class RandomUtilsTest {

    @Test
    void getInt() {

        for (int i = 0; i < 1000 ; i++) {
            int value = RandomUtils.getInstance().getInt(10, 100);
            if (value == 10) {
                log.debug("随机值为：{}", value);
            } else if (value == 100) {
                log.debug("随机值为：{}", value);
            }
            Assert.assertTrue(value >= 10 && value <= 100);

        }


    }
}