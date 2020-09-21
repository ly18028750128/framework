package com.longyou.comm.service;

import com.longyou.comm.model.TFrameUserRef;
import com.longyou.comm.vo.FrameUserRefVO;
import org.cloud.exception.BusinessException;
import org.cloud.model.TFrameUser;
import org.cloud.vo.ResponseResult;

import java.util.List;

public interface FrameUserRefService {

    ResponseResult create(FrameUserRefVO vo) throws BusinessException;

    ResponseResult userCreate(FrameUserRefVO vo) throws BusinessException;

    ResponseResult update(FrameUserRefVO vo) throws BusinessException;

    ResponseResult userUpdate(FrameUserRefVO vo) throws BusinessException;

    TFrameUser selectUserList(Long userId);

    int delete(Long id);

    TFrameUserRef select(Long userId);
}
