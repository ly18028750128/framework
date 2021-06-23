package com.longyou.aihelper.service;

import com.longyou.aihelper.model.Responsetable;
import java.util.List;
public interface ResponsetableService{


    int deleteByPrimaryKey(Long id);

    int insert(Responsetable record);

    int insertOrUpdate(Responsetable record);

    int insertOrUpdateSelective(Responsetable record);

    int insertSelective(Responsetable record);

    Responsetable selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Responsetable record);

    int updateByPrimaryKey(Responsetable record);

    int updateBatch(List<Responsetable> list);

    int batchInsert(List<Responsetable> list);

}
