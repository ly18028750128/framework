package org.cloud.utils;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.crypto.Cipher;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.cloud.core.redis.RedisUtil;

@Slf4j
public final class EccUtil {

    private EccUtil() {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static EccUtil instance = new EccUtil();

    public static EccUtil single() {
        return instance;
    }

    private static final String ALGORITHM = "ECIES";
    private static final String PROVIDER = "BC";

    private static final String CURVE_NAME = "secp256k1";

    public final static String ECC_KEYS_REDIS_KEY = "SYSTEM:CONFIG:ECC_KEYS";


    /**
     * @return 第一条为公钥，第二条为私钥
     * @throws Exception
     */
    public List<String> getEccKey() throws Exception {
        KeyPair keyPair = getKeyPair();
        List<String> keys = new ArrayList<>();
        keys.add(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        keys.add(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        return keys;
    }

    public KeyPair getKeyPair() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER);
        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(CURVE_NAME);
        keyGen.initialize(ecSpec, new SecureRandom());
        return keyGen.generateKeyPair();
    }

    public List<String> getKeyFromRedis() throws Exception {
        List<String> rsaKeys = RedisUtil.single().get(ECC_KEYS_REDIS_KEY);
        if (rsaKeys == null) {
            RedisUtil.single().set(ECC_KEYS_REDIS_KEY, this.getEccKey());
        }
        return RedisUtil.single().get(ECC_KEYS_REDIS_KEY);
    }

    public String encryptFromRedis(String str) throws Exception {
        return this.encrypt(str, getKeyFromRedis().get(0));
    }

    public byte[] encrypt(byte[] data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
        cipher.init(ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    public String encrypt(String str, String publicKeyStr) throws Exception {
        return Base64.getEncoder().encodeToString(this.encrypt(str.getBytes(), convertStringToPublicKey(publicKeyStr)));
    }

    public String decryptFromRedis(String str) throws Exception {
        return this.decrypt(str, getKeyFromRedis().get(1));
    }

    public byte[] decrypt(byte[] encryptedData, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
        cipher.init(DECRYPT_MODE, privateKey);
        return cipher.doFinal(encryptedData);
    }

    public String decrypt(String str, String privateKeyStr) throws Exception {
        return new String(decrypt(Base64.getDecoder().decode(str), convertStringToPrivateKey(privateKeyStr)));
    }

    public String signFromRedis(String str) throws Exception {
        return this.sign(str, getKeyFromRedis().get(1));
    }

    public byte[] sign(byte[] data, PrivateKey privateKey) throws Exception {
        Signature signatureAlgorithm = Signature.getInstance("SHA256withECDSA", PROVIDER);
        signatureAlgorithm.initSign(privateKey);
        signatureAlgorithm.update(data);
        return signatureAlgorithm.sign();
    }

    public String sign(String str, String privateKeyStr) throws Exception {
        return Base64.getEncoder().encodeToString(sign(str.getBytes(), convertStringToPrivateKey(privateKeyStr)));
    }

    public boolean verifySignatureFromRedis(String str, String signature) throws Exception {
        return verifySignature(str, signature, getKeyFromRedis().get(0));
    }

    public boolean verifySignature(byte[] data, byte[] signature, PublicKey publicKey) throws Exception {
        Signature signatureAlgorithm = Signature.getInstance("SHA256withECDSA", PROVIDER);
        signatureAlgorithm.initVerify(publicKey);
        signatureAlgorithm.update(data);
        return signatureAlgorithm.verify(signature);
    }

    public boolean verifySignature(String str, String signature, String publicKey) throws Exception {
        return verifySignature(str.getBytes(), Base64.getDecoder().decode(signature), convertStringToPublicKey(publicKey));
    }


    public PublicKey convertStringToPublicKey(String publicKeyString) throws Exception {
        return convertBytesToPublicKey(Base64.getDecoder().decode(publicKeyString));
    }

    public PublicKey convertBytesToPublicKey(byte[] publicKeyBytes) throws Exception {
        X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePublic(bobPubKeySpec);
    }


    public PrivateKey convertStringToPrivateKey(String privateKeyString) throws Exception {
        return convertStringToPrivateKey(Base64.getDecoder().decode(privateKeyString));
    }

    public PrivateKey convertStringToPrivateKey(byte[] privateKeyBytes) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePrivate(pkcs8EncodedKeySpec);
    }


}
