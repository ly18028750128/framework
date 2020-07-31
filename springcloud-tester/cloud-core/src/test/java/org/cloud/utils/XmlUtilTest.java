package org.cloud.utils;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public final class XmlUtilTest {


    Logger logger = LoggerFactory.getLogger(XmlUtilTest.class);

    @Test
    public void object2XmlString() throws Exception {
        Map<String, Object> test = new HashMap<>();

        test.put("aa", "qqq111");

        test.put("bbb", "22222");

        logger.info(XmlUtil.single().Object2XmlString(test));
    }

    @Test
    public void xmlString2Object() {
    }
}