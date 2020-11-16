package org.cloud.utils;

import org.apache.commons.codec.binary.Base64;
import org.cloud.core.redis.RedisUtil;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import static org.cloud.constant.CoreConstant.RSA_KEYS_REDIS_KEY;

public final class RsaUtil {

    private RsaUtil() {
    }

    private RedisUtil redisUtilService;

    public RedisUtil getRedisUtilService() {
        if (redisUtilService == null) {
            redisUtilService = SpringContextUtil.getBean(RedisUtil.class);
        }
        return redisUtilService;
    }

    private static RsaUtil instance = new RsaUtil();

    public static RsaUtil single() {
        return instance;
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public String decrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        //base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    /**
     * @param keyLength 密钥长度 512-1024
     * @return 第一条为公钥，第二条为私钥
     * @throws Exception
     */
    public List<String> getRsaKey(int keyLength) throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为512-1024位
        keyPairGen.initialize(keyLength, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));

        List<String> keys = new ArrayList<>();

        keys.add(publicKeyString);
        keys.add(privateKeyString);
        return keys;
    }

    public List<String> getRsaKey() throws Exception {
        return this.getRsaKey(1024);
    }

    public String encryptByRedisRsaKey(String str) throws Exception {
        return this.encrypt(str, getRsaKeys().get(0));
    }

    public String decryptByRedisRsaKey(String str) throws Exception {
        return this.decrypt(str, getRsaKeys().get(1));
    }

    public List<String> getRsaKeys() throws Exception {
        List<String> rsaKeys = getRedisUtilService().get(RSA_KEYS_REDIS_KEY);
        if (rsaKeys == null) {
            getRedisUtilService().set(RSA_KEYS_REDIS_KEY, this.getRsaKey(1024));
        }
        return getRedisUtilService().get(RSA_KEYS_REDIS_KEY);
    }


}
