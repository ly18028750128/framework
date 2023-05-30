package org.cloud.utils;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class EccUtilTest {

    @Test
    void single() throws Exception {
        EccUtil eccUtil = EccUtil.single();
        List<String> keys = eccUtil.getEccKey();
        String str = "其实中国才是最厉害的";
        String encStr = eccUtil.encrypt(str, keys.get(0));
        Assertions.assertEquals(eccUtil.decrypt(encStr, keys.get(1)), str);
        String signStr = eccUtil.sign(str, keys.get(1));
        Assertions.assertTrue(eccUtil.verifySignature(str, signStr, keys.get(0)));
        keys = eccUtil.getEccKey();
        Assertions.assertFalse(eccUtil.verifySignature(str, signStr, keys.get(0)));
    }

    @Test
    public void test1() throws Exception {
        Long start = System.nanoTime();

        for (int idx = 0; idx < 10000; idx++) {
            single();
        }

        log.info("call.time={}",System.nanoTime() - start);
    }
}