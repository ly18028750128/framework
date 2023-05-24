package org.cloud.utils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class EccUtilTest {

    @Test
    void single() throws Exception {

        EccUtil eccUtil =  EccUtil.single();

        List<String> keys = eccUtil.getEccKey();


        log.info("keys:{}",keys);

        String str = "这里是中国";


        eccUtil.encrypt(str,keys.get(0));


    }
}