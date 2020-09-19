package com.longyou.comm.service;

import com.longyou.comm.vo.FrameUserRefVO;
import org.cloud.exception.BusinessException;
import org.cloud.vo.ResponseResult;

public interface FrameUserRefService {

    ResponseResult create(FrameUserRefVO vo) throws BusinessException;

    ResponseResult update(FrameUserRefVO vo) throws BusinessException;

}
