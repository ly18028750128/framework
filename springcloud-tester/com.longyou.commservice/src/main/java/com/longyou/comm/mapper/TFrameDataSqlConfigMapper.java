package com.longyou.comm.mapper;

import com.longyou.comm.model.TFrameDataSqlConfig;
import org.springframework.stereotype.Repository;

@Repository
public interface TFrameDataSqlConfigMapper {
    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String dataCode);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(TFrameDataSqlConfig record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(TFrameDataSqlConfig record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    TFrameDataSqlConfig selectByPrimaryKey(String dataCode);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(TFrameDataSqlConfig record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeyWithBLOBs(TFrameDataSqlConfig record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(TFrameDataSqlConfig record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    TFrameDataSqlConfig selectByPrimaryKeyWithLock(String dataCode);
}