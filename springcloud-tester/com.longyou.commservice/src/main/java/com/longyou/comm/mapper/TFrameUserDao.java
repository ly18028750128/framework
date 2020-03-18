package com.longyou.comm.mapper;

import org.cloud.model.TFrameUser;

public interface TFrameUserDao {
    int deleteByPrimaryKey(Long id);

    int insert(TFrameUser record);

    int insertSelective(TFrameUser record);

    TFrameUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TFrameUser record);

    int updateByPrimaryKey(TFrameUser record);
}