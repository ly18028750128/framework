package org.cloud.constant;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class MfaConstantTest {

    @Test
    void valueOf() {
        log.info("{}", MfaConstant.valueOf("CORRELATION_YOUR_GOOGLE_KEY").value());
    }
}