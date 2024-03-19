package com.cloud.parammanage.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloud.parammanage.common.entity.ParamConfig;
import org.apache.ibatis.annotations.Param;

public interface ParamConfigMapper extends BaseMapper<ParamConfig> {
    int insertSelective(ParamConfig record);

    int updateByPrimaryKeySelective(ParamConfig record);

    ParamConfig selectOneByConfigCode(@Param("configCode") String configCode);
}