package com.longyou.comm.service.impl;

import com.longyou.comm.LoginUtils;
import com.longyou.comm.config.MicroAppConfigList;
import com.longyou.comm.mapper.UserInfoMapper;
import com.longyou.comm.service.IUserInfoService;
import java.math.BigInteger;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.Arrays;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.exception.BusinessException;
import org.cloud.userinfo.LoginUserGetInterface;
import org.cloud.vo.LoginUserGetParamsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.utils.Numeric;

/**
 *
 */
@Service(LoginUserGetInterface._LOGIN_USER_GET_PREFIX + "LOGIN-BY-ETH-CHAIN")
@Slf4j
public class EthChainLoginUserGetService implements LoginUserGetInterface {

    @Value("${spring.security.salt-password:}")
    String salt;

    @Value("${system.eth.sign.expire.time:3600000}")
    Long signValueExpireTime = 60 * 60 * 1000L; // 默认60分钟过过期,也就是说30分钟内可以用同一个密钥登录


    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    MicroAppConfigList microAppConfigList;

    @Autowired
    IUserInfoService userInfoService;

    @Autowired
    RedisUtil redisUtil;

    public static final String ADDRESS_SIGN_MAP_KEY = "system:config:ETH-CHAIN-SIGN-MAP:";  // 签名数据存放的map


    @Override
    public LoginUserDetails getUserInfo(LoginUserGetParamsDTO loginUserGetParamsDTO) throws Exception {

        // 先对password进行验证操作
        // 解密用户名，需要提供加密接口，暂定用rsa加密，防止别人抓包后重新登录

        final String address = loginUserGetParamsDTO.getUserName();

        final String addressSignKey = ADDRESS_SIGN_MAP_KEY + address;

        loginUserGetParamsDTO.setUserName(address.toLowerCase(Locale.ROOT));

        final String message = loginUserGetParamsDTO.getParamMap().get("signValue").toString();

        Long expireEndTime = redisUtil.hashGet(addressSignKey, message);
        if (expireEndTime != null && System.currentTimeMillis() > expireEndTime) {
            throw new BusinessException("签名数据已经过期,请重新签名！");
        }

        byte[] msgHash = Hash.sha3((message).getBytes());
        byte[] signatureBytes = Numeric.hexStringToByteArray(loginUserGetParamsDTO.getPassword());
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }

        SignatureData sd =
            new SignatureData(
                v,
                Arrays.copyOfRange(signatureBytes, 0, 32),
                Arrays.copyOfRange(signatureBytes, 32, 64));

        String addressRecovered = null;
        boolean match = false;

        // Iterate for each possible key to recover
        for (int i = 0; i < 4; i++) {
            BigInteger publicKey =
                Sign.recoverFromSignature(
                    (byte) i,
                    new ECDSASignature(
                        new BigInteger(1, sd.getR()), new BigInteger(1, sd.getS())),
                    msgHash);

            if (publicKey != null) {
                addressRecovered = "0x" + Keys.getAddress(publicKey);
                if (addressRecovered.equalsIgnoreCase(address)) {
                    match = true;
                    break;
                }
            }
        }

        if (!match) {
            throw new BusinessException("签名数据验证错误！");
        }

        redisUtil.hashSet(addressSignKey, message, System.currentTimeMillis() + signValueExpireTime, 3600L);

        return LoginUtils.createOrUpdateUserByLoginUserGetParamsDTO(loginUserGetParamsDTO, salt);

    }
}
