package org.cloud.core;

import com.ulisesbocchio.jasyptspringboot.encryptor.SimplePBEStringEncryptor;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.cloud.utils.AES128Util;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.properties.PropertyValueEncryptionUtils;
import org.junit.Assert;
import org.junit.Test;


@Slf4j
public class JasyptLocalTest {

    final String aesKey = "";
    final String aesIV = "";
    final String encKey = "oC0dZ1mU3xI0jG6b";

    @Test
    public void encryptTest() throws Exception {
        pbeByteEncryptor.setPassword(encKey);  // 更改这个密码
        Map<String, String> passwordMap = new LinkedHashMap<>();
        passwordMap.put("mysql用户名：", "Yby8zHKazx1y5Zi2knpYgCWZQcP0KZ");
//        passwordMap.put("mysql密码：", "Yby8zHKazx1y5Zi2knpYgCWZQcP0KZ");
//        passwordMap.put("邮箱用户名：", "");
//        passwordMap.put("邮箱密码：", "");
//        passwordMap.put("mongodb用户名：", "");
//        passwordMap.put("mongodb密码：", "");
//        passwordMap.put("redis密码：", "");
//        passwordMap.put("md5 salt", "");
//        passwordMap.put("aesKey：", aesKey);
//        passwordMap.put("aesIV：", aesIV);
        for (String key : passwordMap.keySet()) {
            String encryptStr = encrypt(passwordMap.get(key));
            Assert.assertEquals(passwordMap.get(key), decrypt(encryptStr));
            log.info("{}\tENC(\"{}\")", key, encryptStr);
        }
    }

    @Test
    public void encrypAesAndEnctTest() throws Exception {
        pbeByteEncryptor.setPassword(encKey);  // 更改这个密码
        Map<String, String> passwordMap = new LinkedHashMap<>();
//        passwordMap.put("withdraw.private-key","");
        passwordMap.put("harvest-private-key", "");
        passwordMap.put("block.signPrivateKey", "");
        for (String key : passwordMap.keySet()) {
            String encryptStr = AES128Util.single().encrypt(passwordMap.get(key), aesKey, aesIV);
            encryptStr = encrypt(encryptStr);
            Assert.assertEquals(passwordMap.get(key), AES128Util.single().decrypt(decrypt(encryptStr), aesKey, aesIV));
            log.info("{}\t{}", key, encryptStr);
        }
    }

    @Test
    public void decryptTest() throws Exception {
        pbeByteEncryptor.setPassword(encKey);  // 更改这个密码
        Map<String, String> passwordMap = new LinkedHashMap<>();
        passwordMap.put("aaa", "4eaugIcddVdWrTJvK36tEXOTGHVhkK155BwYRJyimlEjnpsKnUrMjz4eWK6dA81BOpDgHO5bgPFIMb1nGg3h2g==");
//        passwordMap.put("bbb","");
//        passwordMap.put("mongo","");
//        passwordMap.put("salt","");
        for (String key : passwordMap.keySet()) {
            String encryptStr = decrypt(passwordMap.get(key));
            log.info("{}\t{}", key, encryptStr);
        }
    }


    @Test
    public void decryptTestAesAndEnc() throws Exception {
        pbeByteEncryptor.setPassword(encKey);  // 更改这个密码
        Map<String, String> passwordMap = new LinkedHashMap<>();
        passwordMap.put("harvest-private-key", "");
        passwordMap.put("block.signPrivateKey", "");

        for (String key : passwordMap.keySet()) {
            String encryptStr = decrypt(passwordMap.get(key));
            encryptStr = AES128Util.single().decrypt(encryptStr, aesKey, aesIV);
            log.info("{}\t{}", key, encryptStr);
        }
    }

    private final StringEncryptor basicTextEncryptor;

    final static StandardPBEByteEncryptor pbeByteEncryptor = new StandardPBEByteEncryptor();

    static {
        pbeByteEncryptor.setAlgorithm("PBEWITHHMACSHA512ANDAES_256");
        pbeByteEncryptor.setIvGenerator(new org.jasypt.iv.RandomIvGenerator());
    }

    public JasyptLocalTest() {
        basicTextEncryptor = new SimplePBEStringEncryptor(pbeByteEncryptor);
    }

    /**
     * 明文加密
     *
     * @param plaintext 明文
     * @return String
     */
    private String encrypt(String plaintext) {
        // 使用的加密算法参考2.2节内容，也可以在源码的类注释中看到
        return basicTextEncryptor.encrypt(plaintext);
    }

    /**
     * 解密
     *
     * @param ciphertext 密文
     * @return String
     */
    private String decrypt(String ciphertext) {
        ciphertext = "ENC(" + ciphertext + ")";
        if (PropertyValueEncryptionUtils.isEncryptedValue(ciphertext)) {
            return PropertyValueEncryptionUtils.decrypt(ciphertext, basicTextEncryptor);
        }
        return "";
    }
}
