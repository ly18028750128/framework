package com.longyou.comm.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.longyou.comm.model.ParamConfig;
import org.apache.ibatis.annotations.Param;

public interface ParamConfigMapper extends BaseMapper<ParamConfig> {
    int updateByPrimaryKeySelective(ParamConfig record);

    ParamConfig selectOneByConfigCode(@Param("configCode")String configCode);
}