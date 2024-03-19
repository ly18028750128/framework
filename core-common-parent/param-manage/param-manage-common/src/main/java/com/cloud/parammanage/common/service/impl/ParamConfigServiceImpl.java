package com.cloud.parammanage.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.parammanage.common.entity.ParamConfig;
import com.cloud.parammanage.common.mapper.ParamConfigMapper;
import com.cloud.parammanage.common.service.ParamConfigService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ParamConfigServiceImpl extends ServiceImpl<ParamConfigMapper, ParamConfig> implements ParamConfigService {

    @Override
    public int insertSelective(ParamConfig record) {
        return baseMapper.insertSelective(record);
    }

    @Override
    @CacheEvict(cacheNames = CONFIG_PRAGMA_CACHE_KEY, key = "#record.configCode")
    public int updateByPrimaryKeySelective(ParamConfig record) {
        return baseMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    @Cacheable(cacheNames = CONFIG_PRAGMA_CACHE_KEY, key = "#code")
    public ParamConfig get(String code) {
        return getBaseMapper().selectOneByConfigCode(code);
    }
}

