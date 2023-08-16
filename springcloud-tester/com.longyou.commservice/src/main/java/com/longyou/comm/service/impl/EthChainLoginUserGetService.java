package com.longyou.comm.service.impl;

import static org.cloud.constant.LoginTypeConstant.LoginType.LOGIN_BY_ETH_CHAIN;
import static org.cloud.constant.LoginTypeConstant._LOGIN_BY_ETH_CHAIN;

import com.alibaba.fastjson.JSON;
import com.longyou.comm.LoginUtils;
import com.longyou.comm.dto.EthSignMessageDto;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.Arrays;
import org.cloud.logs.annotation.AuthLog;
import org.cloud.constant.CoreConstant.OperateLogType;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.exception.BusinessException;
import org.cloud.dimension.userinfo.LoginUserGetInterface;
import org.cloud.dimension.userinfo.LoginUserGetParamsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.utils.Numeric;

/**
 *
 */
@Service(LoginUserGetInterface._LOGIN_USER_GET_PREFIX + _LOGIN_BY_ETH_CHAIN)
@Slf4j
public class EthChainLoginUserGetService implements LoginUserGetInterface {

    @Value("${spring.security.salt-password:}")
    private String salt;

    @Value("${system.eth.sign.expire.time:3600000}")
    private Long signValueExpireTime; // 默认60分钟过过期,也就是说60分钟内可以用同一个密钥登录

    @Value("${system.eth.sign.isFormat:true}")
    private Boolean isFormat; // 登录消息是否为EthSignMessageDto的格式


    @Autowired
    private RedisUtil redisUtil;

    public static final String ADDRESS_SIGN_MAP_KEY = "system:config:ETH-CHAIN-SIGN-MAP:";  // 签名数据存放的map
    private static final String MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

    /**
     * @param loginUserGetParamsDTO
     * @return
     * @throws Exception 示例代码 const loginMessage = { address: that.address, webUrl: "http://app.meta-utopia.com", loginTime: new Date().getTime(), message:
     *                   "login in meta-utopia", }
     *                   <p>
     *                   const signValue = JSON.stringify(loginMessage);
     */
    @Override
    @AuthLog(bizType = "getUserInfo", desc = "获取登录用户信息【以太坊签名登录】", operateLogType = OperateLogType.LOG_TYPE_BACKEND)
    public LoginUserDetails getUserInfo(LoginUserGetParamsDTO loginUserGetParamsDTO) throws Exception {
        // 先对password进行验证操作
        // 解密用户名，需要提供加密接口，暂定用rsa加密，防止别人抓包后重新登录
        final String address = loginUserGetParamsDTO.getUserName();
        final String addressSignKey = ADDRESS_SIGN_MAP_KEY + address;
        loginUserGetParamsDTO.setUserName(address.toLowerCase(Locale.ROOT));
        final String message = loginUserGetParamsDTO.getParamMap().get("signValue").toString();

        if (isFormat) {
            EthSignMessageDto ethSignMessageDto = JSON.parseObject(message, EthSignMessageDto.class);
            // 如果登录时间戳不在有效期内，那么提示他重新签名，防止签名被重复利用
            if (System.currentTimeMillis() > ethSignMessageDto.getLoginTime() + signValueExpireTime) {
                throw new BusinessException("eth.login.error.time.expire");
            }
        } else {
            // 用户签名会在redis里记录有效期，如果过期将不在受理这个签名
            Long expireEndTime = redisUtil.hashGet(addressSignKey, message);
            if (expireEndTime != null && System.currentTimeMillis() > expireEndTime) {
                throw new BusinessException("eth.login.error.sign.message.expire");
            }
            // 如果24小时内没有登录过，将所有的签名重置
            if (redisUtil.hashGet(addressSignKey, message) == null) {
                redisUtil.hashSet(addressSignKey, message, System.currentTimeMillis() + signValueExpireTime, 24 * 3600L);
            }
        }

        String prefix = MESSAGE_PREFIX + message.length();
        byte[] msgHash = (prefix + message).getBytes(StandardCharsets.UTF_8);
        byte[] signatureBytes = Numeric.hexStringToByteArray(loginUserGetParamsDTO.getPassword());
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }
        SignatureData sd = new SignatureData(v, Arrays.copyOfRange(signatureBytes, 0, 32), Arrays.copyOfRange(signatureBytes, 32, 64));
        BigInteger recoveredKey = Sign.signedMessageToKey(msgHash, sd);
        String addressRecovered = "0x" + Keys.getAddress(recoveredKey);
        Assert.isTrue(addressRecovered.equalsIgnoreCase(address), "eth.login.error.address.wrong"); // 地址解析错误
        return LoginUtils.createOrUpdateUserByLoginUserGetParamsDTO(loginUserGetParamsDTO, salt);
    }
}
