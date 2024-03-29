package com.unknow.first.utils.image;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.cloud.utils.image.ValidateCodeUtil;
import org.junit.jupiter.api.Test;

@Slf4j
class ValidateCodeUtilTest {

    @Test
    void getRandomCode() {
        log.info("value={}", JSON.toJSON(ValidateCodeUtil.getInstance().getRandomCode()));
    }
}