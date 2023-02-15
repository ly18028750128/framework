package org.cloud.utils;


import java.math.BigDecimal;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5EncoderTest {

    public Logger logger = LoggerFactory.getLogger(MD5EncoderTest.class);

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }


    @Test
    public void encode() {
//        logger.debug(MD5Encoder.encode("abcd1234", "gyV4klmOaVoHRUDv"));
//        logger.debug(MD5Encoder.encode("123456"));

        BigDecimal bigDecimal= BigDecimal.ZERO;

        BigDecimal bigDecimal1= BigDecimal.ONE;

        bigDecimal = bigDecimal1;

        bigDecimal1 = bigDecimal1.add(new BigDecimal(10));

        logger.info("{},{}",bigDecimal,bigDecimal1);

    }


    @org.junit.jupiter.api.Test
    void testEncode() {
    }
}