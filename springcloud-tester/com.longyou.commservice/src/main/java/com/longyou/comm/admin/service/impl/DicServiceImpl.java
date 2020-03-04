package com.longyou.comm.admin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.longyou.comm.admin.service.IDicService;
import com.longyou.comm.mapper.TSystemDicItemMapper;
import com.longyou.comm.mapper.TSystemDicMasterMapper;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;
import org.cloud.model.TSystemDicItem;
import org.cloud.model.TSystemDicMaster;
import org.cloud.vo.QueryParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
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

        LoginUserDetails user = RequestContextManager.single().getRequestContext().getUser();

        int updateCount = 0;
        systemDicMaster.setUpdateDate(new Date());
        systemDicMaster.setUpdateBy(user.getId());
        if (systemDicMaster.getDicMasterId() == null) {
            systemDicMaster.setCreateDate(new Date());
            systemDicMaster.setCreateBy(user.getId());
            updateCount = systemDicMasterMapper.insertSelective(systemDicMaster);
        } else {
            updateCount = systemDicMasterMapper.updateByPrimaryKeySelective(systemDicMaster);
        }

        if (CollectionUtils.isEmpty(systemDicMaster.getItems())) {
            return updateCount;
        }

        for (TSystemDicItem item : systemDicMaster.getItems()) {
            if(item.getDicMasterId()==null){
                item.setDicMasterId(systemDicMaster.getDicMasterId());
            }
            item.setUpdateBy(user.getId());
            item.setUpdateDate(new Date());
            if (item.getDicItemId() == null) {
                item.setCreateDate(new Date());
                item.setCreateBy(user.getId());
                systemDicItemMapper.insertSelective(item);
            } else {
                systemDicItemMapper.updateByPrimaryKeySelective(item);
            }
        }

        return updateCount;
    }

    @Override
    public TSystemDicMaster getDicMasterById(Long dicMasterId) throws Exception {
        TSystemDicMaster systemDicMaster = systemDicMasterMapper.selectByPrimaryKey(dicMasterId);
        if (systemDicMaster != null) {
            systemDicMaster.setItems(systemDicItemMapper.selectByDicMasterId(dicMasterId));
        }
        return systemDicMaster;
    }

    @Override
    public TSystemDicItem getDicItemById(Long dicMasterId) throws Exception {
        return systemDicItemMapper.selectByPrimaryKey(dicMasterId);
    }

    @Override
    public List<TSystemDicItem> getDicItemsByMasterId(Long dicMasterId) throws Exception {
        return systemDicItemMapper.selectByDicMasterId(dicMasterId);
    }

    @Override
    public List<TSystemDicItem> getDicItemsByDicCode(String dicCode, String belongMicroService, String language) throws Exception {
        return systemDicItemMapper.selectByDicCode(dicCode, belongMicroService, language);
    }

    @Override
    public Page<TSystemDicMaster> listPage(QueryParamVO queryParams) throws Exception {
        PageHelper.startPage(queryParams.getPageNum(), queryParams.getPageSize(), "dic_code asc,dic_name asc");
        return systemDicMasterMapper.listPage(queryParams);
    }
}
