package com.longyou.comm.service;

import com.github.pagehelper.Page;
import org.cloud.vo.QueryParamVO;

public interface DataInterfaceService {
    /**
     * 分页查询
     *
     * @param queryParamVO
     * @return
     */
    public Page<?> selectPageList(QueryParamVO queryParamVO);

    public Page<?> selectDataInterfaceParamsPageList(QueryParamVO queryParamVO);

}
