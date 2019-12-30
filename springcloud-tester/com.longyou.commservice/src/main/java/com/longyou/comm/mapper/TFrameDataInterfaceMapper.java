package com.longyou.comm.mapper;

import com.github.pagehelper.Page;
import com.longyou.comm.model.TFrameDataInterface;
import org.cloud.vo.QueryParamVO;
import org.springframework.stereotype.Repository;

@Repository
public interface TFrameDataInterfaceMapper {
    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(String dataCode);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(TFrameDataInterface record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(TFrameDataInterface record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    TFrameDataInterface selectByPrimaryKey(String dataCode);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(TFrameDataInterface record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeyWithBLOBs(TFrameDataInterface record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(TFrameDataInterface record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    TFrameDataInterface selectByPrimaryKeyWithLock(String dataCode);

    Page<TFrameDataInterface> selectPageList(QueryParamVO queryParamVO);

}