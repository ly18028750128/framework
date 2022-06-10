package org.cloud.core;

import com.longyou.comm.starter.CommonServiceApplication;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedHashMap;
import java.util.Map;

//@RunWith(SpringRunner.class)
//

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {CommonServiceApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Slf4j
public class JasyptTest {

    @Autowired
    private StringEncryptor stringEncryptor;

    @Test
    public void encrypt() throws Exception {
        Map<String, String> passwordMap = new LinkedHashMap<>();
        for (String key : passwordMap.keySet()) {
            String encryptStr = stringEncryptor.encrypt(passwordMap.get(key));
            log.info("{}\t{}\t{}", key, passwordMap.get(key), encryptStr);
            Assert.assertEquals(passwordMap.get(key), stringEncryptor.decrypt(encryptStr));
        }
    }


}
