package com.longyou.comm.service;

import com.github.pagehelper.Page;
import com.longyou.comm.model.TFrameUserRef;
import org.cloud.vo.FrameUserRefVO;
import org.cloud.exception.BusinessException;
import org.cloud.vo.QueryParamVO;
import org.cloud.vo.ResponseResult;


public interface FrameUserRefService {

    Integer create(FrameUserRefVO vo) throws BusinessException;

    ResponseResult userCreate(FrameUserRefVO vo) throws BusinessException;

    ResponseResult update(FrameUserRefVO vo) throws BusinessException;

    ResponseResult userUpdate(FrameUserRefVO vo) throws BusinessException;

    TFrameUserRef selectUserList(Long userId);

    int delete(Long id);

    Page<?> select(QueryParamVO queryParamVO);
}
