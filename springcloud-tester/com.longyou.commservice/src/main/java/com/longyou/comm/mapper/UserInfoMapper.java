package com.longyou.comm.mapper;


import org.apache.ibatis.annotations.Param;
import org.cloud.entity.LoginUserDetails;
import org.cloud.vo.LoginUserGetParamsDTO;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface UserInfoMapper {
    LoginUserDetails getUserByNameForAuth(LoginUserGetParamsDTO loginUserGetParamsDTO);
    int insertIntoUserInfo(Map<String,?> params);
}
