package com.longyou.gateway.dao;

import com.longyou.gateway.security.entity.AuthUserDetails;
import org.apache.ibatis.annotations.Param;

public interface IUserInfoDao {
    public AuthUserDetails getUserByName(@Param("userName") String userName);
}
