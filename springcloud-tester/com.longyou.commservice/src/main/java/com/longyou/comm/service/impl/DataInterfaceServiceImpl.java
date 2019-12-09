package com.longyou.comm.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.longyou.comm.mapper.TFrameDataInterfaceMapper;
import com.longyou.comm.mapper.TFrameDataInterfaceParamsMapper;
import com.longyou.comm.model.TFrameDataInterface;
import com.longyou.comm.model.TFrameDataInterfaceParams;
import com.longyou.comm.service.DataInterfaceService;
import org.cloud.vo.QueryParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataInterfaceServiceImpl implements DataInterfaceService {
    @Autowired
    TFrameDataInterfaceMapper tFrameDataInterfaceMapper;

    @Autowired
    TFrameDataInterfaceParamsMapper tFrameDataInterfaceParamsMapper;


    @Override
    public Page<TFrameDataInterface> selectPageList(QueryParamVO queryParamVO) {
        PageHelper.startPage(queryParamVO.getPageNum(), queryParamVO.getPageSize());
        return tFrameDataInterfaceMapper.selectPageList(queryParamVO);
    }

    @Override
    public Page<?> selectDataInterfaceParamsPageList(QueryParamVO queryParamVO) {
        return tFrameDataInterfaceParamsMapper.selectPageList(queryParamVO);
    }
}
