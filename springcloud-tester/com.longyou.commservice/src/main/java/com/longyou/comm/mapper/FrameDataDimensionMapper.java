package com.longyou.comm.mapper;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Param;
import org.cloud.model.FrameDataDimension;
import org.cloud.validator.GroupForAdd;
import org.cloud.validator.GroupForUpdate;
import org.springframework.validation.annotation.Validated;

public interface FrameDataDimensionMapper {

    /**
     * delete by primary key
     *
     * @param dataAuthListId primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(@NotNull Long dataAuthListId);

    /**
     * insert record to table
     *
     * @param record the record
     * @return insert count
     */
    int insert(@Validated(value = GroupForAdd.class) FrameDataDimension record);

    int insertOrUpdate(@Validated(value = GroupForUpdate.class) FrameDataDimension record);

    int insertOrUpdateSelective(@Validated(value = GroupForUpdate.class) FrameDataDimension record);

    /**
     * insert record to table selective
     *
     * @param record the record
     * @return insert count
     */
    int insertSelective(@Validated(value = GroupForAdd.class) FrameDataDimension record);

    /**
     * select by primary key
     *
     * @param dataAuthListId primary key
     * @return object by primary key
     */
    FrameDataDimension selectByPrimaryKey(@NotNull Long dataAuthListId);

    /**
     * update record selective
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(@Validated(value = GroupForUpdate.class) FrameDataDimension record);

    /**
     * update record
     *
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(@Validated(value = GroupForUpdate.class) FrameDataDimension record);

    int updateBatch(List<FrameDataDimension> list);

    int batchInsert(@Param("list") List<FrameDataDimension> list);

    List<FrameDataDimension> selectDataDimensionByUserId(@Param("userId") Long userId);

    List<FrameDataDimension> list(Map<String, Object> params);

}