package com.longyou.comm.mapper;

import org.cloud.model.TFrameRoleDataInterface;

public interface TFrameRoleDataInterfaceDao {
    int deleteByPrimaryKey(Long id);

    int insert(TFrameRoleDataInterface record);

    int insertSelective(TFrameRoleDataInterface record);

    TFrameRoleDataInterface selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TFrameRoleDataInterface record);

    int updateByPrimaryKey(TFrameRoleDataInterface record);
}