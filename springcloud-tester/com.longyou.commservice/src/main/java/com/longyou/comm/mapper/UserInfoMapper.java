package com.longyou.comm.mapper;


import org.apache.ibatis.annotations.Param;
import org.cloud.entity.LoginUserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoMapper {
    LoginUserDetails getUserByName(@Param("userName") String userName);
}
