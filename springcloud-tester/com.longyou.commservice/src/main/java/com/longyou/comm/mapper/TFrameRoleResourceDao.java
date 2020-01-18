package com.longyou.comm.mapper;

import org.cloud.model.TFrameRoleResource;

import java.util.List;

public interface TFrameRoleResourceDao {
    int deleteByPrimaryKey(Long roleResourceId);

    int insert(TFrameRoleResource record);

    int insertSelective(TFrameRoleResource record);

    TFrameRoleResource selectByPrimaryKey(Long roleResourceId);

    int updateByPrimaryKeySelective(TFrameRoleResource record);

    int updateByPrimaryKey(TFrameRoleResource record);

    List<TFrameRoleResource> selectByRoleId(Long roleId);
}