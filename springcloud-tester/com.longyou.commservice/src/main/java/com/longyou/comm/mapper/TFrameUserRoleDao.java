package com.longyou.comm.mapper;

import org.apache.ibatis.annotations.Param;
import org.cloud.model.TFrameUserRole;

import java.util.List;

public interface TFrameUserRoleDao {
    int deleteByPrimaryKey(Long userRoleId);

    int insert(TFrameUserRole record);

    int insertSelective(TFrameUserRole record);

    TFrameUserRole selectByPrimaryKey(Long userRoleId);

    int updateByPrimaryKeySelective(TFrameUserRole record);

    int updateByPrimaryKey(TFrameUserRole record);

    List<TFrameUserRole> selectByUserId(@Param("userId") Long userId);


}