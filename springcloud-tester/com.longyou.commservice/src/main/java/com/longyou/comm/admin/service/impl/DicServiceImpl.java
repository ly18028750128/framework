package com.longyou.comm.admin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.longyou.comm.admin.service.IDicService;
import com.longyou.comm.mapper.TSystemDicItemMapper;
import com.longyou.comm.mapper.TSystemDicMasterMapper;
import org.cloud.model.TSystemDicItem;
import org.cloud.model.TSystemDicMaster;
import org.cloud.vo.QueryParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DicServiceImpl implements IDicService {

    @Autowired
    TSystemDicMasterMapper systemDicMasterMapper;

    @Autowired
    TSystemDicItemMapper systemDicItemMapper;

    @Override
    public int SaveOrUpdate(TSystemDicMaster systemDicMaster) throws Exception {
        return 0;
    }

    @Override
    public TSystemDicMaster getDicMasterById(Long dicMasterId) throws Exception {
        return null;
    }

    @Override
    public TSystemDicItem getDicItemById(Long dicMasterId) throws Exception {
        return null;
    }

    @Override
    public List<TSystemDicItem> getDicItemsByMasterId(Long dicMasterId) throws Exception {
        return null;
    }

    @Override
    public Page<TSystemDicMaster> listPage(QueryParamVO queryParams) throws Exception {
        PageHelper.startPage(queryParams.getPageNum(),queryParams.getPageSize(),"dic_code asc,dic_name asc");
        return systemDicMasterMapper.listPage(queryParams);
    }
}
