package com.longyou.comm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.longyou.comm.model.ParamConfig;


public interface ParamConfigService extends IService<ParamConfig>{

    /**
     * 配置参数缓存key
     */
    String CONFIG_PRAGMA_CACHE_KEY = "param:config";


    int updateByPrimaryKeySelective(ParamConfig record);


    ParamConfig get(String code);
}
