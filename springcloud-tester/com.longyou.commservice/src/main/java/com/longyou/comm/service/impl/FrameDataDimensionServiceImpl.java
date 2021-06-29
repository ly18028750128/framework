package com.longyou.comm.service.impl;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.cloud.validator.GroupForAdd;
import org.cloud.validator.GroupForUpdate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.longyou.comm.mapper.FrameDataDimensionMapper;
import java.util.List;
import org.cloud.model.FrameDataDimension;
import com.longyou.comm.service.FrameDataDimensionService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
public class FrameDataDimensionServiceImpl implements FrameDataDimensionService {

    @Resource
    private FrameDataDimensionMapper frameDataDimensionMapper;

    @Override
    public int deleteByPrimaryKey(@NotNull Long dataAuthListId) {
        return frameDataDimensionMapper.deleteByPrimaryKey(dataAuthListId);
    }

    @Override
    public int insert(@Validated(value = GroupForAdd.class) FrameDataDimension record) {
        return frameDataDimensionMapper.insert(record);
    }

    @Override
    public int insertOrUpdate(@Validated(value = GroupForUpdate.class) FrameDataDimension record) {
        return frameDataDimensionMapper.insertOrUpdate(record);
    }

    @Override
    @Transactional
    public int insertOrUpdateBatch(@Size(min = 1) List<FrameDataDimension> records) {
        records.forEach(frameDataDimension -> frameDataDimensionMapper.insertOrUpdateSelective(frameDataDimension));
        return records.size();
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

    @Override
    public List<FrameDataDimension> selectDataDimensionByUserId(String dataDimensionType, Long referId) {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("dataDimensionType", dataDimensionType);
        params.put("referId", referId);
        return frameDataDimensionMapper.list(params);
    }

}
