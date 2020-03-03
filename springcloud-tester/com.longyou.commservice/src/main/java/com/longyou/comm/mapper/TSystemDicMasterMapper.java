package com.longyou.comm.mapper;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.cloud.model.TSystemDicItem;
import org.cloud.model.TSystemDicMaster;
import org.cloud.vo.QueryParamVO;
import org.springframework.stereotype.Repository;

@Repository
public interface TSystemDicMasterMapper {

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(@Param("dicMasterId") Long dicMasterId);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(TSystemDicMaster record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(TSystemDicMaster record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    TSystemDicMaster selectByPrimaryKey(@Param("dicMasterId") Long dicMasterId);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(TSystemDicMaster record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(TSystemDicMaster record);

    Page<TSystemDicMaster> listPage(QueryParamVO queryParamVO);
}