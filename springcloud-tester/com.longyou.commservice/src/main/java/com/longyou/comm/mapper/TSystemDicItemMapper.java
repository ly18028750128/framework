package com.longyou.comm.mapper;

import org.cloud.model.TSystemDicItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TSystemDicItemMapper {

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByPrimaryKey(Long dicItemId);

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
    TSystemDicItem selectByPrimaryKey(Long dicItemId);

    List<TSystemDicItem> selectByDicMasterId(Long dicMasterId);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKeySelective(TSystemDicItem record);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByPrimaryKey(TSystemDicItem record);
}