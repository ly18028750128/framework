package com.longyou.comm.mapper;

import com.github.pagehelper.Page;
import com.longyou.comm.model.TFrameDataSpConfig;
import org.cloud.vo.QueryParamVO;
import org.springframework.stereotype.Repository;

@Repository
public interface TFrameDataSpConfigMapper {
    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String dataCode);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(TFrameDataSpConfig record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(TFrameDataSpConfig record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    TFrameDataSpConfig selectByPrimaryKey(String dataCode);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(TFrameDataSpConfig record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeyWithBLOBs(TFrameDataSpConfig record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(TFrameDataSpConfig record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    TFrameDataSpConfig selectByPrimaryKeyWithLock(String dataCode);

    Page<TFrameDataSpConfig> selectPageList(QueryParamVO params);
}