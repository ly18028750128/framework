package org.cloud.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class DESUtil {

    /**
     * 偏移变量，固定占8位字节
     */
    private static String IV_PARAMETER = "ynxfekge";

    private static String _COMMON_DES_PASSWORD = "sdvfwvxnfjsldxwzycklhi189412sdn123%%&^/232";  //全局的des加密的密码，也可以在各个应用中，对需要进行加密的数据进行加密，比如说数据字典
    /**
     * 加密/解密算法-工作模式-填充模式
     */
    private final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";
    /**
     * 默认编码
     */
    private final String CHARSET = "utf-8";

    /**
     * 生成key
     *
     * @param password
     * @return
     * @throws Exception
     */
    private Key generateKey(String password) throws Exception {
        DESKeySpec dks = new DESKeySpec(password.getBytes(CHARSET));
        /**
         * 密钥算法
         */
        String ALGORITHM = "DES";
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(dks);
    }


    /**
     * DES加密字符串
     *
     * @param password 加密密码，长度不能够小于8位
     * @param data     待加密字符串
     * @return 加密后内容
     */
    public String encrypt(String password, String data) {
        if (password == null || password.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        if (data == null) {
            return null;
        }
        try {
            Key secretKey = generateKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] bytes = cipher.doFinal(data.getBytes(CHARSET));

            //JDK1.8及以上可直接使用Base64，JDK1.7及以下可以使用BASE64Encoder
            //Android平台可以使用android.util.Base64
            return new String(Base64.getEncoder().encode(bytes));

        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    public String encrypt(String data) {
        return encrypt(_COMMON_DES_PASSWORD, data);
    }

    /**
     * DES解密字符串
     *
     * @param password 解密密码，长度不能够小于8位
     * @param data     待解密字符串
     * @return 解密后内容
     */
    public String decrypt(String password, String data) {
        if (password == null || password.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        if (data == null) {
            return null;
        }
        try {
            Key secretKey = generateKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            return new String(cipher.doFinal(Base64.getDecoder().decode(data.getBytes(CHARSET))), CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            return data;
        }
    }

    public String decrypt(String data) {
        return decrypt(_COMMON_DES_PASSWORD, data);
    }

    /**
     * DES加密文件
     *
     * @param srcFile  待加密的文件
     * @param destFile 加密后存放的文件路径
     * @return 加密后的文件路径
     */
    public String encryptFile(String password, String srcFile, String destFile) {

        if (password == null || password.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        try {
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, generateKey(password), iv);
            InputStream is = new FileInputStream(srcFile);
            OutputStream out = new FileOutputStream(destFile);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = cis.read(buffer)) > 0) {
                out.write(buffer, 0, r);
            }
            cis.close();
            is.close();
            out.close();
            return destFile;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    /**
     * DES解密文件
     *
     * @param srcFile  已加密的文件
     * @param destFile 解密后存放的文件路径
     * @return 解密后的文件路径
     */
    public String decryptFile(String password, String srcFile, String destFile) {
        if (password == null || password.length() < 8) {
            throw new RuntimeException("加密失败，key不能小于8位");
        }
        try {
            File file = new File(destFile);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, generateKey(password), iv);
            InputStream is = Files.newInputStream(Paths.get(srcFile));
            OutputStream out = Files.newOutputStream(Paths.get(destFile));
            CipherOutputStream cos = new CipherOutputStream(out, cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = is.read(buffer)) >= 0) {
                cos.write(buffer, 0, r);
            }
            cos.close();
            is.close();
            out.close();
            return destFile;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }


    private static DESUtil desUtil=new DESUtil(IV_PARAMETER,_COMMON_DES_PASSWORD);

    public DESUtil(String ivParameter, String commonDesPassword) {
        if (SystemStringUtil.single().isNotEmpty(ivParameter)) {
            DESUtil.IV_PARAMETER = ivParameter;
        }
        if (SystemStringUtil.single().isNotEmpty(commonDesPassword)) {
            DESUtil._COMMON_DES_PASSWORD = commonDesPassword;
        }
        desUtil = this;
    }

    public static DESUtil single() {
        return desUtil;
    }
}
