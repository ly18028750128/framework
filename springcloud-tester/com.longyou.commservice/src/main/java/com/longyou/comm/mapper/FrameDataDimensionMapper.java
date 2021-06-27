package com.longyou.comm.mapper;

import org.cloud.model.FrameDataDimension;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FrameDataDimensionMapper {

  /**
   * delete by primary key
   *
   * @param dataAuthListId primaryKey
   * @return deleteCount
   */
  int deleteByPrimaryKey(Long dataAuthListId);

  /**
   * insert record to table
   *
   * @param record the record
   * @return insert count
   */
  int insert(FrameDataDimension record);

  int insertOrUpdate(FrameDataDimension record);

  int insertOrUpdateSelective(FrameDataDimension record);

  /**
   * insert record to table selective
   *
   * @param record the record
   * @return insert count
   */
  int insertSelective(FrameDataDimension record);

  /**
   * select by primary key
   *
   * @param dataAuthListId primary key
   * @return object by primary key
   */
  FrameDataDimension selectByPrimaryKey(Long dataAuthListId);

  /**
   * update record selective
   *
   * @param record the updated record
   * @return update count
   */
  int updateByPrimaryKeySelective(FrameDataDimension record);

  /**
   * update record
   *
   * @param record the updated record
   * @return update count
   */
  int updateByPrimaryKey(FrameDataDimension record);

  int updateBatch(List<FrameDataDimension> list);

  int batchInsert(@Param("list") List<FrameDataDimension> list);

  List<FrameDataDimension> selectDataDimensionByUserId(@Param("userId") Long userId);

}