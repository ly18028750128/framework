package com.longyou.comm.service.impl;

import com.longyou.comm.mapper.UserInfoMapper;
import com.longyou.comm.service.IUserInfoService;
import org.cloud.entity.LoginUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService implements IUserInfoService {

    @Autowired
    UserInfoMapper userInfoMapper ;

    @Override
    public LoginUserDetails getUserByNameForAuth(String userName) {
        return userInfoMapper.getUserByNameForAuth(userName);
    }
}
