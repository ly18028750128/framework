import lombok.extern.slf4j.Slf4j;
import org.cloud.utils.AES128Util;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

@Slf4j
class AES128UtilTest {

  @Test
  void encrypt() throws Exception {

    log.info("==={}",-7%(-3));
    
//    final String str = "fbd09ad9b076091e18ca5ac72555b5dd96ec37973e328142db124da0de6e896a";
//    final String aesKey = "&Nb0jLwmfsqh&5O2";
//    final String aesIV = ")vF(mOuY$E7Ma7vb";

    final String str = "";
    final String aesKey = "";
    final String aesIV = "";

//    final String str = "41c63d72dd27a6328959b18bf8d2955c8407d37c40abb2cc225ee820d64f2fc6";
//    final String aesKey = "sE0!bF0!bB2$dN0!";
//    final String aesIV = "nE0}gD6;fF2@dB2%";




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