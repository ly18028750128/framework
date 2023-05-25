package org.cloud.utils;

import java.security.KeyPair;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class EccUtilTest {

    @Test
    void single() throws Exception {

        EccUtil eccUtil = EccUtil.single();

        List<String> keys = eccUtil.getEccKey();

        log.info("keys:{}", keys);

        String str = "这里是中国";

        String encStr = eccUtil.encrypt(str, keys.get(0));
        log.info("加密:{}", encStr);
        log.info("解密:{}", eccUtil.decrypt(encStr, keys.get(1)));


        String signStr = eccUtil.sign(str, keys.get(1));
        log.info("签名:{}", signStr);

        log.info("签名验证:{}", eccUtil.verifySignature(str,signStr,keys.get(0)));
    }
}