package com.longyou.comm.mapper;

import org.apache.ibatis.annotations.Param;
import org.cloud.model.TFrameworkResource;

public interface TFrameworkResourceDao {
    int deleteByPrimaryKey(@Param("resourceId") Long resourceId);

    int insert(TFrameworkResource record);

    int insertSelective(TFrameworkResource record);

    TFrameworkResource selectByPrimaryKey(@Param("resourceId") Long resourceId);

    TFrameworkResource selectByResourceCode(@Param("resourceCode") String resourceCode );

    int updateByPrimaryKeySelective(TFrameworkResource record);

    int updateByPrimaryKey(TFrameworkResource record);
}