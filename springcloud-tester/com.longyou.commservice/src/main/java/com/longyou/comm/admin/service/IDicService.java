package com.longyou.comm.admin.service;

import com.github.pagehelper.Page;
import org.cloud.model.TSystemDicItem;
import org.cloud.model.TSystemDicMaster;
import org.cloud.vo.QueryParamVO;

import java.util.List;

public interface IDicService {

    public int SaveOrUpdate(TSystemDicMaster systemDicMaster) throws Exception;

    public TSystemDicMaster getDicMasterById( Long dicMasterId) throws Exception;

    public TSystemDicItem getDicItemById(Long dicMasterId) throws Exception;

    public List<TSystemDicItem> getDicItemsByMasterId(Long dicMasterId) throws Exception;

    public List<TSystemDicItem> getDicItemsByDicCode(String dicCode,String belongMicroService,String language) throws Exception;

    public Page<TSystemDicMaster> listPage(QueryParamVO queryParams) throws Exception;

}
