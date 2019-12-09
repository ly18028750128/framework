package com.longyou.comm.service;

import org.cloud.entity.LoginUserDetails;

public interface IUserInfoService {
    public LoginUserDetails getUserByName(String userName);
}
