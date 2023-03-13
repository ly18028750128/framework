# 脚本使用用说明
## 执行脚本
1. mysql初脚化脚本采用nb3的格式，也就是navicat的备份，请找到最新的备份，目前版本为20230313
2. mongodb初始化脚本包含了消息和定时任务日志的脚本及动态定义的数据接口的脚本
## 初始化密码更改方法
使用MD5EncoderTest的encode方法，<b>注意要输入正确的盐值</b>
```java
package org.cloud.utils;
import java.math.BigDecimal;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MD5EncoderTest {

    public Logger logger = LoggerFactory.getLogger(MD5EncoderTest.class);

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }


    @Test
    public void encode() {
        logger.debug("encodePassword = {}",MD5Encoder.encode("abcd1234", "盐值"));
    }


    @org.junit.jupiter.api.Test
    void testEncode() {
    }
}
```
