package org.cloud.core;

import com.ulisesbocchio.jasyptspringboot.encryptor.SimplePBEStringEncryptor;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.properties.PropertyValueEncryptionUtils;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class JasyptLocalTest {

    @Test
    public void encryptTest() throws Exception {

        pbeByteEncryptor.setPassword("");  // 更改这个密码

        Map<String, String> passwordMap = new LinkedHashMap<>();


        for (String key : passwordMap.keySet()) {
            String encryptStr = encrypt(passwordMap.get(key));
            log.info("{}\t{}\t{}", key, passwordMap.get(key), encryptStr);
            Assert.assertEquals(passwordMap.get(key), decrypt(encryptStr));
        }
    }

    //    private final PBEByteEncryptor pbeByteEncryptor = new SimplePBEByteEncryptor();
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
