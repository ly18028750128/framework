package com.cloud.parammanage.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cloud.parammanage.common.entity.ParamConfig;

public interface ParamConfigService extends IService<ParamConfig> {

    /**
     * 配置参数缓存key
     */
    String CONFIG_PRAGMA_CACHE_KEY = "param:config";


    ParamConfig get(String code);

    int insertSelective(ParamConfig record);

    int updateByPrimaryKeySelective(ParamConfig record);
}

