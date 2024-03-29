package com.longyou.comm.service.login.impl;


import static org.cloud.constant.CoreConstant.RSA_KEYS_REDIS_KEY;
import static org.cloud.constant.LoginTypeConstant._LOGIN_BY_THIRD_LOGIN;
import static org.cloud.constant.LoginTypeConstant._LOGIN_BY_VIRTUAL_USER;

import com.longyou.comm.LoginUtils;
import com.longyou.comm.config.MicroAppConfigList;
import com.longyou.comm.mapper.UserInfoMapper;
import com.longyou.comm.service.IUserInfoService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.cloud.constant.CoreConstant.OperateLogType;
import org.cloud.core.redis.RedisUtil;
import org.cloud.dimension.userinfo.LoginUserGetInterface;
import org.cloud.dimension.userinfo.LoginUserGetParamsDTO;
import org.cloud.entity.LoginUserDetails;
import org.cloud.logs.annotation.AuthLog;
import org.cloud.utils.RsaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service(LoginUserGetInterface._LOGIN_USER_GET_PREFIX + _LOGIN_BY_THIRD_LOGIN)
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
    @AuthLog(bizType = "getUserInfo", desc = "获取登录用户信息【第三方外部登录】", operateLogType = OperateLogType.LOG_TYPE_BACKEND)
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
