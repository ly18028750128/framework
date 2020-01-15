package com.longyou.comm.mapper;

import org.apache.ibatis.annotations.Param;
import org.cloud.model.TMicroserviceRegister;

public interface TMicroserviceRegisterDao {
    int deleteByPrimaryKey(@Param("microserviceId") Integer microserviceId);

    int insert(TMicroserviceRegister record);

    int insertSelective(TMicroserviceRegister record);

    TMicroserviceRegister selectByPrimaryKey(@Param("microserviceId") Integer microserviceId);

    TMicroserviceRegister selectByMicroserviceName(@Param("microserviceName") String microserviceName);

    int updateByPrimaryKeySelective(TMicroserviceRegister record);

    int updateByPrimaryKey(TMicroserviceRegister record);
}