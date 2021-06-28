package com.longyou.aihelper.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.longyou.aihelper.model.Responsetable;
import java.util.List;
import com.longyou.aihelper.mapper.ResponsetableMapper;
import com.longyou.aihelper.service.ResponsetableService;
@Service
public class ResponsetableServiceImpl implements ResponsetableService{

    @Resource
    private ResponsetableMapper responsetableMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return responsetableMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(Responsetable record) {
        return responsetableMapper.insert(record);
    }

    @Override
    public int insertOrUpdate(Responsetable record) {
        return responsetableMapper.insertOrUpdate(record);
    }

    @Override
    public int insertOrUpdateSelective(Responsetable record) {
        return responsetableMapper.insertOrUpdateSelective(record);
    }

    @Override
    public int insertSelective(Responsetable record) {
        return responsetableMapper.insertSelective(record);
    }

    @Override
    public Responsetable selectByPrimaryKey(Long id) {
        return responsetableMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Responsetable record) {
        return responsetableMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Responsetable record) {
        return responsetableMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateBatch(List<Responsetable> list) {
        return responsetableMapper.updateBatch(list);
    }

    @Override
    public int batchInsert(List<Responsetable> list) {
        return responsetableMapper.batchInsert(list);
    }

}
