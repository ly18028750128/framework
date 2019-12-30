package com.longyou.comm.mapper;

import com.github.pagehelper.Page;
import com.longyou.comm.model.TFrameDataInterface;
import com.longyou.comm.model.TFrameDataInterfaceParams;
import org.apache.ibatis.annotations.Param;
import org.cloud.vo.QueryParamVO;
import org.springframework.stereotype.Repository;

@Repository
public interface TFrameDataInterfaceParamsMapper {
    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(@Param("dataCode") String dataCode, @Param("seq") Integer seq, @Param("paramName") String paramName);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(TFrameDataInterfaceParams record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(TFrameDataInterfaceParams record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    TFrameDataInterfaceParams selectByPrimaryKey(@Param("dataCode") String dataCode, @Param("seq") Integer seq, @Param("paramName") String paramName);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(TFrameDataInterfaceParams record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(TFrameDataInterfaceParams record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    TFrameDataInterfaceParams selectByPrimaryKeyWithLock(@Param("dataCode") String dataCode, @Param("seq") Integer seq, @Param("paramName") String paramName);

    Page<TFrameDataInterfaceParams> selectPageList(QueryParamVO queryParamVO);
}