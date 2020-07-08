package org.cloud.core;

import com.longyou.comm.starter.CommonServiceApplication;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.servlet.annotation.WebInitParam;

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
        String[] yuanWenArr = {"heyichenshijueceRedis@)20", "heyichenshijueceMysql@)20", "heyichenshijueceMongo@)20"
                , "heyichenshijuecePostgis@)20", "heyichenshijueceGeoserver@)20", "想暴力破解没门",
                "admin", "root", "postgres", "2f7b0f31bf2d9017e78a2ab78604cb0e"
        };

//        String[] yuanWenArr = {"1qaz@WSX",
//                "admin", "root", "postgres", "2f7b0f31bf2d9017e78a2ab78604cb0e"
//        };

        for (String yuanWen : yuanWenArr) {
            String encryptStr = stringEncryptor.encrypt(yuanWen);
            log.info("{}={}", yuanWen, encryptStr);
            log.info("{}={}", encryptStr, stringEncryptor.decrypt(encryptStr));
        }
    }

}
