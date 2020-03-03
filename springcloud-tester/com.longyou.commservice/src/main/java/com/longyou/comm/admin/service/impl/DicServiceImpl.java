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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Service
public class DicServiceImpl implements IDicService {

    @Autowired
    TSystemDicMasterMapper systemDicMasterMapper;

    @Autowired
    TSystemDicItemMapper systemDicItemMapper;

    @Override
    @Transactional
    public int SaveOrUpdate(TSystemDicMaster systemDicMaster) throws Exception {
        int updateCount = 0;
        if (systemDicMaster.getDicMasterId() == null) {
            updateCount = systemDicMasterMapper.insert(systemDicMaster);
        } else {
            updateCount = systemDicMasterMapper.updateByPrimaryKeySelective(systemDicMaster);
        }

        if (CollectionUtils.isEmpty(systemDicMaster.getItems())) {
            return updateCount;
        }

        for (TSystemDicItem item : systemDicMaster.getItems()) {
            item.setDicMasterId(systemDicMaster.getDicMasterId());
            if (item.getDicItemId() == null) {
                systemDicItemMapper.insert(item);
            } else {
                systemDicItemMapper.updateByPrimaryKeySelective(item);
            }
        }

        return updateCount;
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
        return systemDicItemMapper.selectByDicMasterId(dicMasterId);
    }

    @Override
    public List<TSystemDicItem> getDicItemsByDicCode(String dicCode) throws Exception {
        return systemDicItemMapper.selectByDicCode(dicCode);
    }

    @Override
    public Page<TSystemDicMaster> listPage(QueryParamVO queryParams) throws Exception {
        PageHelper.startPage(queryParams.getPageNum(), queryParams.getPageSize(), "dic_code asc,dic_name asc");
        return systemDicMasterMapper.listPage(queryParams);
    }
}
