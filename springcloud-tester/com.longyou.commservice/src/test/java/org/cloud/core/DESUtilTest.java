package org.cloud.core;

import org.cloud.utils.DESUtil;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class DESUtilTest {

    Logger logger = LoggerFactory.getLogger(DESUtilTest.class);

    private String _COMMON_DES_PASSWORD = "justTEST!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!";

    /**
     * 加密的时候用
     */
    @Test
    public void encrypt() {
        String noEnStr = "Huangtushengtai20190708huangtuly";
        String enStr = DESUtil.encrypt(noEnStr);
        logger.info("加密后的密码为："+enStr);
    }

    @Test
    public void decrypt() {
        String noEnStr = "12312213123123131";
        String enStr = DESUtil.encrypt(_COMMON_DES_PASSWORD, noEnStr);
        Assert.assertTrue(noEnStr.equals(DESUtil.decrypt(_COMMON_DES_PASSWORD, enStr)));

        enStr = DESUtil.encrypt( noEnStr);
        Assert.assertTrue(noEnStr.equals(DESUtil.decrypt(enStr)));
    }

}
