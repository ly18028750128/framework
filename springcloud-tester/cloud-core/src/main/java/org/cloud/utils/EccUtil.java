package org.cloud.utils;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.crypto.Cipher;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.cloud.core.redis.RedisUtil;

public final class EccUtil {

    private EccUtil() {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static EccUtil instance = new EccUtil();

    public static EccUtil single() {
        return instance;
    }

    private static final String ALGORITHM = "EC";
    private static final String PROVIDER = "BC";

    private static final String CURVE_NAME = "secp256k1";

    public final static String ECC_KEYS_REDIS_KEY = "SYSTEM:CONFIG:ECC_KEYS";


    /**
     * @return 第一条为公钥，第二条为私钥
     * @throws Exception
     */
    public List<String> getEccKey() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER);
        ECGenParameterSpec ecSpec = new ECGenParameterSpec(CURVE_NAME);
        keyGen.initialize(ecSpec);
        KeyPair keyPair = keyGen.generateKeyPair();
        List<String> keys = new ArrayList<>();
        keys.add(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        keys.add(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        return keys;
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
        return new String(this.encrypt(str.getBytes("UTF-8"), convertStringToPublicKey(publicKeyStr)));
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
        byte[] inputByte = Base64.getDecoder().decode(str.getBytes("UTF-8"));
        return new String(decrypt(inputByte, convertStringToPrivateKey(privateKeyStr)));
    }

    public String signFromRedis(String str) throws Exception {
        return this.sign(str, getKeyFromRedis().get(1));
    }

    public byte[] sign(byte[] data, PrivateKey privateKey) throws Exception {
        Signature signatureAlgorithm = Signature.getInstance(ALGORITHM, PROVIDER);
        signatureAlgorithm.initSign(privateKey);
        signatureAlgorithm.update(data);
        return signatureAlgorithm.sign();
    }

    public String sign(String str, String publicKey) throws Exception {
        byte[] inputByte = Base64.getDecoder().decode(str.getBytes("UTF-8"));
        return new String(sign(inputByte, convertStringToPrivateKey(publicKey)));
    }

    public boolean verifySignatureFromRedis(String str, String signature) throws Exception {
        return verifySignature(str, signature, getKeyFromRedis().get(0));
    }

    public static boolean verifySignature(byte[] data, byte[] signature, PublicKey publicKey) throws Exception {
        Signature signatureAlgorithm = Signature.getInstance(ALGORITHM, PROVIDER);
        signatureAlgorithm.initVerify(publicKey);
        signatureAlgorithm.update(data);
        return signatureAlgorithm.verify(signature);
    }

    public boolean verifySignature(String str, String signature, String publicKey) throws Exception {
        byte[] inputByte = Base64.getDecoder().decode(str.getBytes("UTF-8"));
        return verifySignature(inputByte, signature.getBytes("UTF-8"), convertStringToPublicKey(publicKey));
    }


    public PublicKey convertStringToPublicKey(String publicKeyString) throws Exception {
        return convertBytesToPublicKey(Base64.getDecoder().decode(publicKeyString));
    }

    private PublicKey convertBytesToPublicKey(byte[] publicKeyBytes) throws Exception {
        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(CURVE_NAME); // 使用secp256k1曲线
        ECPublicKeySpec pubKeySpec = new ECPublicKeySpec(ecSpec.getCurve().decodePoint(publicKeyBytes), ecSpec);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, PROVIDER);
        return keyFactory.generatePublic(pubKeySpec);
    }

    public PrivateKey convertStringToPrivateKey(String privateKeyString) throws Exception {
        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(CURVE_NAME); // 使用secp256k1曲线
        ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(new BigInteger(1, Base64.getDecoder().decode(privateKeyString)), ecSpec);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, PROVIDER);
        return keyFactory.generatePrivate(privateKeySpec);
    }



}
