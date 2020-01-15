package com.longyou.comm.mapper;

import org.apache.ibatis.annotations.Param;
import org.cloud.model.TFrameMenu;

public interface TFrameMenuDao {
    int deleteByPrimaryKey(@Param("menuId") Long menuId);
    int insert(TFrameMenu record);
    int insertSelective(TFrameMenu record);
    TFrameMenu selectByPrimaryKey(@Param("menuId") Long menuId);
    TFrameMenu selectByMenuCode(@Param("menuCode") String menuCode);
    int updateByPrimaryKeySelective(TFrameMenu record);
    int updateByPrimaryKey(TFrameMenu record);
}