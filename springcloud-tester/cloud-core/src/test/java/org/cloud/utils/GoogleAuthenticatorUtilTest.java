package org.cloud.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.io.*;

@Slf4j
class GoogleAuthenticatorUtilTest {
    GoogleAuthenticatorUtil googleAuthenticatorUtil = GoogleAuthenticatorUtil.single();

    @Test
    void getGoogleAuthenticatorBarCode() throws Exception {
        final String secretKey = googleAuthenticatorUtil.generateSecretKey();
        log.info("secretKey={}", secretKey);
        log.info("getGoogleAuthenticatorBarCode={}", googleAuthenticatorUtil.getQRBarcode("test:Account", secretKey));
    }


    @Test
    void checkCode() throws Exception {
        //FFF6QN7DSDD7R5VNRNCFLB23VQ42Q62B
        final String secretKey = "VWOZKBWODKHTV6Y2Z2UCNDP7";
        final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in)) ;
        log.info("请输入验证码：");
        final String code = reader.readLine();
//        googleAuthenticatorUtil.setWindowSize(5);
        Assert.isTrue(googleAuthenticatorUtil.checkCode(secretKey,Long.parseLong(code) , System.currentTimeMillis()),"验证失败！"+code);

    }
}