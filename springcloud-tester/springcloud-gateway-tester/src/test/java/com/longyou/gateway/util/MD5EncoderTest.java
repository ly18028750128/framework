package com.longyou.gateway.util;



import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

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
        logger.debug(MD5Encoder.encode("123456","想暴力破解没门"));
        logger.debug(MD5Encoder.encode("123456"));
    }


}