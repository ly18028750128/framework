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
        passwordMap.put("mysql", "isI17^2jM11oP*a2#1qq");
        passwordMap.put("mongodb", "chuangkekongjian309Mongodb@)20");
        passwordMap.put("Redis", "chuangkekongjian309Redis@)20");
        passwordMap.put("consul", "chuangkekongjian309Consul@)20");
        passwordMap.put("root", "root");
        passwordMap.put("rcm3pro", "rcm3pro");
        passwordMap.put("admin", "admin");
        passwordMap.put("9PRKmKQo", "9PRKmKQo");

        for (String key : passwordMap.keySet()) {
            String encryptStr = stringEncryptor.encrypt(passwordMap.get(key));
            log.info("{},{}={}", key, passwordMap.get(key), encryptStr);

            Assert.assertEquals(passwordMap.get(key), stringEncryptor.decrypt(encryptStr));

//            log.info("{},{}={}", key, encryptStr, stringEncryptor.decrypt(encryptStr));
        }
    }

}
