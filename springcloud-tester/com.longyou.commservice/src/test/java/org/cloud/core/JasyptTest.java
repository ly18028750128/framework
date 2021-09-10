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
//        String[] yuanWenArr = {"heyichenshijueceRedis@)20", "heyichenshijueceMysql@)20", "heyichenshijueceMongo@)20"
//                , "heyichenshijuecePostgis@)20", "heyichenshijueceGeoserver@)20", "想暴力破解没门",
//                "admin", "root", "postgres", "2f7b0f31bf2d9017e78a2ab78604cb0e"
//        };


        Map<String, String> passwordMap = new LinkedHashMap<>();
        passwordMap.put("mongodb用户", "");
        passwordMap.put("mongodb密码", "");
        passwordMap.put("mysql用户", "");
        passwordMap.put("mysql密码", "");
        passwordMap.put("wallet-mysql用户", "");
        passwordMap.put("wallet-mysql密码", "");
        passwordMap.put("Redis密码", "");
        passwordMap.put("kuangji-AES-KEY", "");
        passwordMap.put("kuangji-AES-IV", "");



        for (String key : passwordMap.keySet()) {
            String encryptStr = stringEncryptor.encrypt(passwordMap.get(key));
            log.info("{}\t{}\t{}", key, passwordMap.get(key), encryptStr);

            Assert.assertEquals(passwordMap.get(key), stringEncryptor.decrypt(encryptStr));

//            log.info("{},{}={}", key, encryptStr, stringEncryptor.decrypt(encryptStr));
        }
    }



}
