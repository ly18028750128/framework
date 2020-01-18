package com.longyou.comm.mapper;

import org.apache.ibatis.annotations.Param;
import org.cloud.model.TFrameRole;

public interface TFrameRoleDao {
    int deleteByPrimaryKey(@Param("roleId") Integer roleId);

    int insert(TFrameRole record);

    int insertSelective(TFrameRole record);

    TFrameRole selectByPrimaryKey(@Param("roleId") Integer roleId);

    TFrameRole selectByRoleCode(@Param("roleCode") String roleCode);

    int updateByPrimaryKeySelective(TFrameRole record);

    int updateByPrimaryKey(TFrameRole record);
}