package utils;


import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.cloud.utils.RsaUtil;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class RsaUtilTest {

    @Test
    public void encryptAndDecrypt() throws Exception {

        List<String> keys = RsaUtil.single().getRsaKey();
        final String testStr = "其实中国才是最厉害的";
        final String encryptStr = RsaUtil.single().encrypt(testStr, keys.get(0));
        Assert.assertEquals(testStr,RsaUtil.single().decrypt(encryptStr, keys.get(1)));
        final String signStr = RsaUtil.single().sign(testStr, keys.get(1));
        Assert.assertTrue(RsaUtil.single().verifySignature(testStr, signStr, keys.get(0)));

        keys = RsaUtil.single().getRsaKey();

        Assert.assertFalse(RsaUtil.single().verifySignature(testStr, signStr, keys.get(0)));
    }
//    @Test
//    public void test1() throws Exception {
//        Long start = System.nanoTime();
//        for (int idx = 0; idx < 10000; idx++) {
//            encryptAndDecrypt();
//        }
//        log.info("call.time={}",System.nanoTime() - start);
//    }
}