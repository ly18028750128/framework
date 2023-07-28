package org.cloud.utils;

import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.cloud.encdec.service.AESService;
import org.springframework.context.annotation.Lazy;

public final class AES128Util {

    private final AESService aesService;

    @Lazy
    private AES128Util() {
        this.aesService = SpringContextUtil.getBean(AESService.class);
    }


    private final static AES128Util AES_128_UTIL = new AES128Util();

    public static AES128Util single() {
        return AES_128_UTIL;
    }

    //算法名
    private final String KEY_ALGORITHM = "AES";
    /**
     * 加解密算法/模式/填充方式 可以任意选择，为了方便后面与iOS端的加密解密，采用与其相同的模式与填充方式 ECB模式只用密钥即可对数据进行加密解密，CBC模式需要添加一个参数iv
     */


    private final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

    //生成密钥

    private byte[] generateKey(String aesKey) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        return aesKey.getBytes();
    }

    //生成iv
    private AlgorithmParameters generateIV(String ivVal) throws Exception {
        //iv 为一个 16 字节的数组，这里采用和 iOS 端一样的构造方法，数据全为0
        //byte[] iv = new byte[16];
        //Arrays.fill(iv, (byte) 0x00);
        //Arrays.fill(iv,ivVal.getBytes());
        byte[] iv = ivVal.getBytes();
        AlgorithmParameters params = AlgorithmParameters.getInstance(KEY_ALGORITHM);
        params.init(new IvParameterSpec(iv));
        return params;
    }

    //转化成JAVA的密钥格式
    private Key convertToKey(byte[] keyBytes) throws Exception {
        return new SecretKeySpec(keyBytes, KEY_ALGORITHM);
    }

    /**
     * 加密
     */
    public String encrypt(String plainText, String aesKey, String ivVal) throws Exception {
        byte[] data = plainText.getBytes();
        AlgorithmParameters iv = generateIV(ivVal);
        byte[] keyBytes = generateKey(aesKey);
        //转化为密钥
        Key key = convertToKey(keyBytes);
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        //设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encryptData = cipher.doFinal(data);
        return bytesToHexString(encryptData);
    }

    public String encrypt(String plainText) throws Exception {
        return aesService.encrypt(plainText);
    }

    /**
     * 解密
     *
     * @param encryptedStr
     * @param aesKey
     * @param ivVal
     * @return
     * @throws Exception
     */

    public String decrypt(String encryptedStr, String aesKey, String ivVal) throws Exception {
        byte[] encryptedData = hexStringToByte(encryptedStr);
        byte[] keyBytes = generateKey(aesKey);
        Key key = convertToKey(keyBytes);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        AlgorithmParameters iv = generateIV(ivVal);
        //设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] decryptData = cipher.doFinal(encryptedData);
        return new String(decryptData);
    }

    public String decrypt(String encryptedStr) throws Exception {
        return aesService.decrypt(encryptedStr);
    }

    /**
     * 十六进制字符串转换成数组
     *
     * @param hex
     * @return
     */

    private byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }


    private byte toByte(char c) {
        return (byte) "0123456789abcdef".indexOf(c);
    }


    /**
     * 把字节数组转换成16进制字符串
     *
     * @param bArray
     * @return
     */

    private final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toLowerCase());
        }
        return sb.toString();
    }
}
