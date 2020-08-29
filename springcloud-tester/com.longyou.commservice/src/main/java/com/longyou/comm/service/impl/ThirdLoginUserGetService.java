package com.longyou.comm.service.impl;


import com.longyou.comm.config.MicroAppConfigList;
import com.longyou.comm.mapper.UserInfoMapper;
import com.longyou.comm.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.cloud.constant.CoreConstant;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.userinfo.LoginUserGetInterface;
import org.cloud.utils.MD5Encoder;
import org.cloud.utils.RsaUtil;
import org.cloud.vo.LoginUserGetParamsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.cloud.constant.CoreConstant.RSA_KEYS_REDIS_KEY;


@Service(LoginUserGetInterface._LOGIN_USER_GET_PREFIX + "LOGIN-BY-THIRD-LOGIN")
@Slf4j
public class ThirdLoginUserGetService implements LoginUserGetInterface {

    @Value("${spring.security.salt-password:}")
    String salt;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    MicroAppConfigList microAppConfigList;

    @Autowired
    IUserInfoService userInfoService;

    @Autowired
    RedisUtil redisUtil;

    @Override
    @Transactional
    public LoginUserDetails getUserInfo(LoginUserGetParamsDTO loginUserGetParamsDTO) throws Exception {
        List<String> rsaKeys = redisUtil.get(RSA_KEYS_REDIS_KEY);
        String realUserName = loginUserGetParamsDTO.getUserName();
        try{
            realUserName = RsaUtil.single().decrypt(realUserName, rsaKeys.get(1));
        }catch (Exception e){
            log.error("解密失败，按明文用户处理！");
        }
        final String password = MD5Encoder.encode(loginUserGetParamsDTO.getPassword(), salt);  // 先用用户名做校验
        final String appName = loginUserGetParamsDTO.getMicroServiceName();
        loginUserGetParamsDTO.setUserName(realUserName);
        // 设置成获取的用户名
        loginUserGetParamsDTO.setUserName(realUserName);
        loginUserGetParamsDTO.getParamMap().put(CoreConstant._USER_TYPE_KEY, appName);
        LoginUserDetails userDetails = userInfoMapper.getUserByNameForAuth(loginUserGetParamsDTO);

        if (userDetails == null) {
            // TODO 保存到t_frame_user表里
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userName", realUserName);
            userMap.put("password", password);
            userMap.put("createBy", realUserName);
            userMap.put("updateBy", realUserName);
            userMap.put("status", 1);
            userMap.put("defaultRole", "User");
            userMap.put("userType", loginUserGetParamsDTO.getLoginType());
            userMap.put("userRegistSource", appName);
            userMap.put("sessionKey", "");
            userInfoMapper.insertIntoUserInfo(userMap);
        } else {
            userDetails.setPassword(password);
            userDetails.setSessionKey(appName);
            userInfoMapper.updateLoginUserById(userDetails);
        }
        userDetails = userInfoService.getUserByNameForAuth(loginUserGetParamsDTO);
        return userDetails;
    }
}
