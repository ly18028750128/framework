package com.longyou.comm.service;

import org.cloud.entity.LoginUserDetails;
import org.cloud.vo.LoginUserGetParamsDTO;

import java.util.Map;

public interface IUserInfoService {
    public LoginUserDetails getUserByNameForAuth(LoginUserGetParamsDTO loginUserGetParamsDTO) throws Exception;

    public int updatePassword(String oldPassword,String password) throws Exception;


}
