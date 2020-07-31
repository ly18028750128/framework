package org.cloud.utils;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;

@Slf4j
public class RsaUtilTest {

    @Test
    public void encryptAndDecrypt() throws Exception {

        List<String> keys = RsaUtil.single().getRsaKey();

        log.info("publicKeyString=" + keys.get(0));
        log.info("privateKeyString=" + keys.get(1));

//        final String publicKeyString = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAITIVVr8MQSHR010eqB106SpY+SwTiBR3CF0ACcIQaoYc6Ah5he7F4SwGSZ2UYc7ZJN1+e2I2ngJUHKaaQ/JL4kCAwEAAQ==";
//        final String privateKeyString = "MIIBVgIBADANBgkqhkiG9w0BAQEFAASCAUAwggE8AgEAAkEAhMhVWvwxBIdHTXR6oHXTpKlj5LBOIFHcIXQAJwhBqhhzoCHmF7sXhLAZJnZRhztkk3X57YjaeAlQcpppD8kviQIDAQABAkBcvkyX+1QFdLOBzxyjnQjRlxrVrasz+dlGaG5+1M6AjgRISdFMvDviM4d76RVBL8uQysy2FD0I1j9b6FSGDZ0tAiEA2sI+AfccPoq0Jz6eA/4xIlp2Sjh0VUXpndyuWW9blvcCIQCbYyZCrvzVN0J5gZwKv09jzYHq8zx9Ye7icL6TAVdNfwIhAJzUdK/kAJO1zMH12kLykTcXs4YyQvR/UqSh+TyU4QyxAiEAhCbR7lqWatuBbIc/Z6CG63FnMaPGaTg2C10ppyq3zg8CIQCtX209QrDqNFIMlk6XubBbYJDlx5atEEHe/5Z+gg86tQ==";


        final String testStr = "abc123";

        final String encryptStr = RsaUtil.single().encrypt(testStr, keys.get(0));

        log.info("encryptStr=" + encryptStr);

        final String decryptStr = RsaUtil.single().decrypt(encryptStr, keys.get(1));

        log.info("decryptStr=" + decryptStr);
        Assert.assertTrue(testStr.equals(decryptStr));

    }


    @Test
    public void getKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(512, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        log.info("publicKeyString=" + publicKeyString);
        log.info("privateKeyString=" + privateKeyString);
    }
}