package org.cloud.utils;


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
        logger.debug(MD5Encoder.encode("tina30112009@JET", "adv8kI1jhai51kxS"));
        logger.debug(MD5Encoder.encode("123456"));
    }


    @org.junit.jupiter.api.Test
    void testEncode() {
    }
}