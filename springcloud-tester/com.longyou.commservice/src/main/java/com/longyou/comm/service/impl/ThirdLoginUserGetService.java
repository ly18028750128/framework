package com.longyou.comm.service.impl;


import com.longyou.comm.LoginUtils;
import com.longyou.comm.config.MicroAppConfigList;
import com.longyou.comm.mapper.UserInfoMapper;
import com.longyou.comm.service.IUserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.cloud.annotation.AuthLog;
import org.cloud.constant.CoreConstant;
import org.cloud.constant.CoreConstant.OperateLogType;
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
    @AuthLog(bizType = "getUserInfo", desc = "获取登录用户信息", operateLogType = OperateLogType.LOG_TYPE_BACKEND)
    public LoginUserDetails getUserInfo(LoginUserGetParamsDTO loginUserGetParamsDTO) throws Exception {
        List<String> rsaKeys = redisUtil.get(RSA_KEYS_REDIS_KEY);
        String realUserName = loginUserGetParamsDTO.getUserName();
        try{
            realUserName = RsaUtil.single().decrypt(realUserName, rsaKeys.get(1));
            loginUserGetParamsDTO.setUserName(realUserName);
        }catch (Exception e){
            log.error("解密失败，按明文用户处理！");
        }
        return LoginUtils.createOrUpdateUserByLoginUserGetParamsDTO(loginUserGetParamsDTO, salt);
    }
}
