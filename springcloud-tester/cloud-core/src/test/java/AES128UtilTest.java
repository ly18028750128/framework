import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.cloud.utils.AES128Util;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class AES128UtilTest {

    @Test
    void encrypt() throws Exception {

        final String aesKey = "TW=>3S.z:9/;p5Im";
        final String aesIV = "I)LNx.wf:2zM'sZ(";

        Map<String, String> valuesMap = new LinkedHashMap<>();
        valuesMap.put("trx-操作地址私钥", "ef345894aa6d5285ce410ea87e9566aa0f408916ad3aeebadd5ac7289d5daa70");
        for (String key : valuesMap.keySet()) {
            final String str = valuesMap.get(key);
            final String encStr = AES128Util.single().encrypt(str, aesKey, aesIV);
            log.info("[{}]加密后={}", key, encStr);
            final String decStr = AES128Util.single().decrypt(encStr, aesKey, aesIV);
            Assertions.assertEquals(str, decStr);
        }
    }

    @Test
    void decrypt() throws Exception {
        final String aesKey = "";
        final String aesIV = "";
        Map<String, String> valuesMap = new LinkedHashMap<>();
        valuesMap.put("googole", "");
        for (String key : valuesMap.keySet()) {
            final String str = valuesMap.get(key);
            final String decStr = AES128Util.single().decrypt(str, aesKey, aesIV);
            log.info("[{}]解密后={}", key, decStr);
        }


    }
}