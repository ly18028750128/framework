package com.longyou.comm.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.longyou.comm.mapper.FrameDataDimensionMapper;
import java.util.List;
import org.cloud.model.FrameDataDimension;
import com.longyou.comm.service.FrameDataDimensionService;

@Service
public class FrameDataDimensionServiceImpl implements FrameDataDimensionService {

  @Resource
  private FrameDataDimensionMapper frameDataDimensionMapper;

  @Override
  public int deleteByPrimaryKey(Long dataAuthListId) {
    return frameDataDimensionMapper.deleteByPrimaryKey(dataAuthListId);
  }

  @Override
  public int insert(FrameDataDimension record) {
    return frameDataDimensionMapper.insert(record);
  }

  @Override
  public int insertOrUpdate(FrameDataDimension record) {
    return frameDataDimensionMapper.insertOrUpdate(record);
  }

  @Override
  public int insertOrUpdateSelective(FrameDataDimension record) {
    return frameDataDimensionMapper.insertOrUpdateSelective(record);
  }

  @Override
  public int insertSelective(FrameDataDimension record) {
    return frameDataDimensionMapper.insertSelective(record);
  }

  @Override
  public FrameDataDimension selectByPrimaryKey(Long dataAuthListId) {
    return frameDataDimensionMapper.selectByPrimaryKey(dataAuthListId);
  }

  @Override
  public int updateByPrimaryKeySelective(FrameDataDimension record) {
    return frameDataDimensionMapper.updateByPrimaryKeySelective(record);
  }

  @Override
  public int updateByPrimaryKey(FrameDataDimension record) {
    return frameDataDimensionMapper.updateByPrimaryKey(record);
  }

  @Override
  public int updateBatch(List<FrameDataDimension> list) {
    return frameDataDimensionMapper.updateBatch(list);
  }

  @Override
  public int batchInsert(List<FrameDataDimension> list) {
    return frameDataDimensionMapper.batchInsert(list);
  }




  @Override
  public List<FrameDataDimension> selectDataDimensionByUserId(Long userId) {
    return frameDataDimensionMapper.selectDataDimensionByUserId(userId);
  }



}
