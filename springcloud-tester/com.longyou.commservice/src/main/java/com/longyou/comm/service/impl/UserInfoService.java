package com.longyou.comm.service.impl;

import com.longyou.comm.mapper.UserInfoMapper;
import com.longyou.comm.service.IUserInfoService;
import org.cloud.entity.LoginUserDetails;
import org.cloud.vo.LoginUserGetParamsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserInfoService implements IUserInfoService {

    @Autowired
    UserInfoMapper userInfoMapper ;

    @Override
    public LoginUserDetails getUserByNameForAuth(LoginUserGetParamsDTO loginUserGetParamsDTO) throws Exception {
        return userInfoMapper.getUserByNameForAuth(loginUserGetParamsDTO);
    }
}
