package com.longyou.comm.mapper;


import org.cloud.entity.LoginUserDetails;
import org.cloud.dimension.userinfo.LoginUserGetParamsDTO;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface UserInfoMapper {
    LoginUserDetails getUserByNameForAuth(LoginUserGetParamsDTO loginUserGetParamsDTO) throws Exception;

    int insertIntoUserInfo(Map<String, ?> params) throws Exception;

    int updateLoginUserById(LoginUserDetails loginUserDetails) throws Exception;
}
