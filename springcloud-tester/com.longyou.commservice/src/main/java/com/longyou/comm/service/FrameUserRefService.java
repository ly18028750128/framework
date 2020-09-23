package com.longyou.comm.service;

import com.github.pagehelper.Page;
import com.longyou.comm.model.TFrameUserRef;
import com.longyou.comm.vo.FrameUserRefVO;
import org.cloud.exception.BusinessException;
import org.cloud.model.TFrameUser;
import org.cloud.vo.QueryParamVO;
import org.cloud.vo.ResponseResult;


public interface FrameUserRefService {

    ResponseResult create(FrameUserRefVO vo) throws BusinessException;

    ResponseResult userCreate(FrameUserRefVO vo) throws BusinessException;

    ResponseResult update(FrameUserRefVO vo) throws BusinessException;

    ResponseResult userUpdate(FrameUserRefVO vo) throws BusinessException;

    TFrameUserRef selectUserList(String userId);

    int delete(Long id);

    Page<?> select(QueryParamVO queryParamVO);
}
