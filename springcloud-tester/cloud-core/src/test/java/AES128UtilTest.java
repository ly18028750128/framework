import lombok.extern.slf4j.Slf4j;
import org.cloud.utils.AES128Util;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

@Slf4j
class AES128UtilTest {

  @Test
  void encrypt() throws Exception {

    log.info("==={}",-7%(-3));
    
    final String str = "测试加密aelweflew的革佻您珍有世在夫角硒鼓苇有";
    final String aesKey = "EK1*Q#Ba7i]\"_m^n";
    final String aesIV = "4{*x}tT0S<W`CK,L";
    final String encStr = AES128Util.single().encrypt(str,aesKey,aesIV);

    log.info("enStr={}",encStr);
    final String decStr = AES128Util.single().decrypt(encStr,aesKey,aesIV);
    log.info("decStr={}",decStr);
    Assert.assertEquals(str,decStr);
  }

  @Test
  void decrypt() {
  }
}