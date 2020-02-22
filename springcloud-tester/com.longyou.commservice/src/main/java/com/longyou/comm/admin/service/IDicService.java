package com.longyou.comm.admin.service;

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.cloud.model.TSystemDicItem;
import org.cloud.model.TSystemDicMaster;
import org.cloud.vo.QueryParamVO;

import java.util.List;

public interface IDicService {

    public int SaveOrUpdate(TSystemDicMaster systemDicMaster) throws Exception;

    public TSystemDicMaster getDicMasterById(@Param("dicMasterId") Long dicMasterId) throws Exception;

    public TSystemDicItem getDicItemById(@Param("dicItemId") Long dicMasterId) throws Exception;

    public List<TSystemDicItem> getDicItemsByMasterId(@Param("dicMasterId") Long dicMasterId) throws Exception;

    public Page<TSystemDicMaster> listPage(QueryParamVO queryParams) throws Exception;

}
