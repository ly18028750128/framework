package com.longyou.comm.mapper;

import org.apache.ibatis.annotations.Param;
import org.cloud.model.TFrameRoleMenu;

import java.util.List;

public interface TFrameRoleMenuDao {
    int deleteByPrimaryKey(@Param("roleMenuId") Long roleMenuId);

    int insert(TFrameRoleMenu record);

    int insertSelective(TFrameRoleMenu record);

    TFrameRoleMenu selectByPrimaryKey(@Param("roleMenuId") Long roleMenuId);

    List<TFrameRoleMenu> selectByRoleId(@Param("roleId") Long roleId);

    int updateByPrimaryKeySelective(TFrameRoleMenu record);

    int updateByPrimaryKey(TFrameRoleMenu record);
}