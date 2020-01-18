package com.longyou.comm.mapper;

import org.apache.ibatis.annotations.Param;
import org.cloud.model.TFrameRoleData;

public interface TFrameRoleDataDao {
    int deleteByPrimaryKey(@Param("dataAuthListId") Integer dataAuthListId);

    int insert(TFrameRoleData record);

    int insertSelective(TFrameRoleData record);

    TFrameRoleData selectByPrimaryKey(@Param("dataAuthListId") Integer dataAuthListId);

    TFrameRoleData selectByRoleId(@Param("roleId") Integer roleId);

    int updateByPrimaryKeySelective(TFrameRoleData record);

    int updateByPrimaryKey(TFrameRoleData record);
}