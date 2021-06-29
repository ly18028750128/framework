package com.longyou.comm.service;

import java.util.List;
import org.cloud.model.FrameDataDimension;

public interface FrameDataDimensionService {


  int deleteByPrimaryKey(Long dataAuthListId);

  int insert(FrameDataDimension record);

  int insertOrUpdate(FrameDataDimension record);

  int insertOrUpdateSelective(FrameDataDimension record);

  int insertSelective(FrameDataDimension record);

  FrameDataDimension selectByPrimaryKey(Long dataAuthListId);

  int updateByPrimaryKeySelective(FrameDataDimension record);

  int updateByPrimaryKey(FrameDataDimension record);

  int updateBatch(List<FrameDataDimension> list);

  int batchInsert(List<FrameDataDimension> list);

  List<FrameDataDimension> selectDataDimensionByUserId(Long userId);

  List<FrameDataDimension> selectDataDimensionByUserId(String dataDimensionType, Long referId);

}
