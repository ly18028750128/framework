package org.cloud.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;
import org.cloud.constant.MfaConstant;
import org.cloud.context.RequestContext;
import org.cloud.context.RequestContextManager;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.exception.BusinessException;
import org.cloud.feign.service.ICommonServiceFeignClient;
import org.cloud.mybatis.dynamic.DynamicSqlUtil;
import org.cloud.vo.DynamicSqlQueryParamsVO;
import org.cloud.vo.FrameUserRefVO;
import org.cloud.vo.JavaBeanResultMap;
import org.springframework.http.HttpStatus;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.cloud.config.MfaFilterConfig.__MFA_TOKEN_USER_GOOGLE_SECRET_CACHE_KEY;

/**
 * google身份验证器，java服务端实现
 *
 * @author yangbo
 * @version 创建时间：2017年8月14日 上午10:10:02
 */
@Slf4j
public final class GoogleAuthenticatorUtil {

    private GoogleAuthenticatorUtil() {

    }

    private final static GoogleAuthenticatorUtil INSTANCE = new GoogleAuthenticatorUtil();

    public static GoogleAuthenticatorUtil single() {
        return INSTANCE;
    }

    // 生成的key长度( Generate secret key length)
    public final int SECRET_SIZE = 15;

    public final String SEED = "g8GjEvTbW5oVSV7avL47357438reyhreyuryetredLDVKs2m0QN7vxRs2im5MDaNCWGmcD2rvcZx";
    // Java实现随机数算法
    public final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";
    // 最多可偏移的时间
    int window_size = 3; // default 3 - max 17

    /**
     * set the windows size. This is an integer value representing the number of
     * 30 second windows we allow The bigger the window, the more tolerant of
     * clock skew we are.
     *
     * @param s window size - must be >=1 and <=17. Other values are ignored
     */
    public void setWindowSize(int s) {
        if (s >= 1 && s <= 17)
            window_size = s;
    }

    /**
     * Generate a random secret key. This must be saved by the server and
     * associated with the users account to verify the code displayed by Google
     * Authenticator. The user must register this secret on their device.
     * 生成一个随机秘钥
     *
     * @return secret key
     */
    public String generateSecretKey() {
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstance(RANDOM_NUMBER_ALGORITHM);
            sr.setSeed(Base64.decodeBase64(SEED));
            byte[] buffer = sr.generateSeed(SECRET_SIZE);
            Base32 codec = new Base32();
            byte[] bEncodedKey = codec.encode(buffer);
            String encodedKey = new String(bEncodedKey);
            return encodedKey;
        } catch (NoSuchAlgorithmException e) {
            // should never occur... configuration error
        }
        return null;
    }

    /**
     * Return a URL that generates and displays a QR barcode. The user scans
     * this bar code with the Google Authenticator application on their
     * smartphone to register the auth code. They can also manually enter the
     * secret if desired
     *
     * @param user   user id (e.g. fflinstone)
     * @param host   host or system that the code is for (e.g. myapp.com)
     * @param secret the secret that was previously generated for this user
     * @return the URL for the QR code to scan
     */
    @SneakyThrows
    public String getQRBarcodeURL(String user, String host, String secret) {
        final String otpauth = "otpauth://totp/%s@%s?secret=%s";
        final String barCodeApiUrl = "https://api.pwmqr.com/qrcode/create/?url=";
        return barCodeApiUrl + URLEncoder.encode(String.format(otpauth, user, host, secret), "utf8");
    }

    /**
     * 生成一个google身份验证器，识别的字符串，只需要把该方法返回值生成二维码扫描就可以了。
     *
     * @param user   账号
     * @param secret 密钥
     * @return
     */
    public String getQRBarcode(String user, String secret) {
        String format = "otpauth://totp/%s?secret=%s";
        return String.format(format, user, secret);
    }

    /**
     * Check the code entered by the user to see if it is valid 验证code是否合法
     *
     * @param secret   The users secret.
     * @param code     The code displayed on the users device
     * @param timeMsec The time in msec (System.currentTimeMillis() for example)
     * @return
     */
    public boolean checkCode(String secret, long code, long timeMsec) {
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(secret);
        long t = (timeMsec / 1000L) / 30L;
        for (int i = -window_size; i <= window_size; ++i) {
            long hash;
            try {
                hash = verifyCode(decodedKey, t + i);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (hash == code) {
                return true;
            }
        }
        return false;
    }

    private int verifyCode(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }
        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);
        int offset = hash[20 - 1] & 0xF;
        // We're using a long because Java hasn't got unsigned int.
        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            // We are dealing with signed bytes:
            // we just keep the first byte.
            truncatedHash |= (hash[offset + i] & 0xFF);
        }
        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;
        return (int) truncatedHash;
    }

    ICommonServiceFeignClient commonServiceFeignClient;

    private ICommonServiceFeignClient getCommonServiceFeignClient() {
        if (commonServiceFeignClient == null) {
            commonServiceFeignClient = SpringContextUtil.getBean(ICommonServiceFeignClient.class);
        }
        return commonServiceFeignClient;
    }

    RedisUtil redisUtil;

    private RedisUtil getRedisUtil() {
        if (redisUtil == null) {
            redisUtil = SpringContextUtil.getBean(RedisUtil.class);
        }
        return redisUtil;
    }

    public String getCurrentUserVerifyKey() throws Exception {
        RequestContext currentRequestContext = RequestContextManager.single().getRequestContext();
        LoginUserDetails user = currentRequestContext.getUser();
        String googleSecret = getRedisUtil().get(__MFA_TOKEN_USER_GOOGLE_SECRET_CACHE_KEY + user.getId());

        if (CollectionUtil.single().isNotEmpty(googleSecret)) {
            return googleSecret;
        }

        DynamicSqlQueryParamsVO dynamicSqlQueryParamsVO = new DynamicSqlQueryParamsVO();
        dynamicSqlQueryParamsVO.getParams().put("userId", user.getId());
        dynamicSqlQueryParamsVO.getParams().put("attributeName", MfaConstant._GOOGLE_MFA_USER_SECRET_REF_ATTR_NAME);

        final String sqlForQueryUserGoogleKey = "select * from t_frame_user_ref where attribute_name = #{attributeName,jdbcType=VARCHAR} " +
                "and  user_id = #{userId,jdbcType=VARCHAR}";

        List<JavaBeanResultMap<Object>> resultMaps = DynamicSqlUtil.single().listDataBySqlContext(sqlForQueryUserGoogleKey, dynamicSqlQueryParamsVO);
        //  如果未绑定谷歌验证那么插入谷歌验证属性
        if (CollectionUtil.single().isEmpty(resultMaps)) {
            googleSecret = GoogleAuthenticatorUtil.single().generateSecretKey();
            FrameUserRefVO frameUserRefVO = new FrameUserRefVO();
            frameUserRefVO.setAttributeName(MfaConstant._GOOGLE_MFA_USER_SECRET_REF_ATTR_NAME);
            frameUserRefVO.setUserId(user.getId());
            frameUserRefVO.setAttributeValue(googleSecret);
            frameUserRefVO.setCreateBy("admin");
            frameUserRefVO.setUpdateBy("admin");
            frameUserRefVO.setRemark("谷歌验证码");
            getCommonServiceFeignClient().addUserRef(frameUserRefVO);
            final Map<String, String> exceptionObject = new LinkedHashMap<>();
            exceptionObject.put("description", MfaConstant.CORRELATION_YOUR_GOOGLE_KEY.description());
            exceptionObject.put("secret", googleSecret);
            exceptionObject.put("secretQRBarcode", this.getQRBarcode(user.getUsername(), googleSecret));
            exceptionObject.put("secretQRBarcodeURL", this.getQRBarcodeURL(user.getUsername(), "", googleSecret));
            getRedisUtil().set(__MFA_TOKEN_USER_GOOGLE_SECRET_CACHE_KEY + user.getId(), googleSecret, -1L);
            throw new BusinessException(MfaConstant.CORRELATION_YOUR_GOOGLE_KEY.value(), exceptionObject, HttpStatus.BAD_REQUEST.value()); //
            // 谷歌key
        }
        googleSecret = (String) resultMaps.get(0).get("attributeValue");
        getRedisUtil().set(__MFA_TOKEN_USER_GOOGLE_SECRET_CACHE_KEY + user.getId(), googleSecret, -1L);
        return googleSecret;
    }

    public Boolean checkGoogleVerifyCode(String googleSecret) throws BusinessException {
        final String mfaValue = HttpServletUtil.signle().getHttpServlet().getHeader(MfaConstant.MFA_HEADER_NAME.value());
        // 请输入谷歌验证码
        if (CollectionUtil.single().isEmpty(mfaValue)) {
            throw new BusinessException(MfaConstant.CORRELATION_GOOGLE_VERIFY_CODE_ISNULL.value(),
                    MfaConstant.CORRELATION_GOOGLE_VERIFY_CODE_ISNULL.description(), HttpStatus.BAD_REQUEST.value());
        }
        // 请输入谷歌验证码
        if (!NumericUtil.single().isLong(mfaValue)) {
            throw new BusinessException(MfaConstant.CORRELATION_GOOGLE_VERIFY_CODE_NOT_NUMERIC.value(),
                    MfaConstant.CORRELATION_GOOGLE_VERIFY_CODE_NOT_NUMERIC.description(), HttpStatus.BAD_REQUEST.value());
        }

        boolean isVerifyPass = GoogleAuthenticatorUtil.single().checkCode(googleSecret, Long.parseLong(mfaValue),
                System.currentTimeMillis());

        if (!isVerifyPass) {
            throw new BusinessException(MfaConstant.CORRELATION_GOOGLE_VERIFY_FAILED.value(),
                    MfaConstant.CORRELATION_GOOGLE_VERIFY_FAILED.description(), HttpStatus.BAD_REQUEST.value());
        }
        return true;
    }

}
