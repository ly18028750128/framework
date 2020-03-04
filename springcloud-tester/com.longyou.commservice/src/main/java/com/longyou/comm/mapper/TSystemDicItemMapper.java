package com.longyou.comm.mapper;

import org.apache.ibatis.annotations.Param;
import org.cloud.model.TSystemDicItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TSystemDicItemMapper {

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(@Param("dicItemId") Long dicItemId);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(TSystemDicItem record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(TSystemDicItem record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    TSystemDicItem selectByPrimaryKey(@Param("dicItemId") Long dicItemId);

    List<TSystemDicItem> selectByDicMasterId(@Param("dicMasterId") Long dicMasterId);

    List<TSystemDicItem> selectByDicCode(@Param("dicCode") String dicCode,@Param("belongMicroService") String belongMicroService,@Param("language") String language);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(TSystemDicItem record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(TSystemDicItem record);
}