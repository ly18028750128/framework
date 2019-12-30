package com.longyou.comm.mapper;

import com.longyou.comm.model.TFrameDataRestConfig;
import org.springframework.stereotype.Repository;

@Repository
public interface TFrameDataRestConfigMapper {
    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String dataCode);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(TFrameDataRestConfig record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(TFrameDataRestConfig record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    TFrameDataRestConfig selectByPrimaryKey(String dataCode);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(TFrameDataRestConfig record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeyWithBLOBs(TFrameDataRestConfig record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(TFrameDataRestConfig record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    TFrameDataRestConfig selectByPrimaryKeyWithLock(String dataCode);
}