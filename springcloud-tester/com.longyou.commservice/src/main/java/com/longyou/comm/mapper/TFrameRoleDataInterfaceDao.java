package com.longyou.comm.mapper;

import org.apache.ibatis.annotations.Param;
import org.cloud.model.TFrameRoleDataInterface;

import java.util.List;

public interface TFrameRoleDataInterfaceDao {
    int deleteByPrimaryKey(Long id);

    int insert(TFrameRoleDataInterface record);

    int insertSelective(TFrameRoleDataInterface record);

    TFrameRoleDataInterface selectByPrimaryKey(Long id);

    List<TFrameRoleDataInterface> selectByRoleId(Long id);

    int deleteByRoleId(@Param("roleId") int roleId);

    int updateByPrimaryKeySelective(TFrameRoleDataInterface record);

    int updateByPrimaryKey(TFrameRoleDataInterface record);
}