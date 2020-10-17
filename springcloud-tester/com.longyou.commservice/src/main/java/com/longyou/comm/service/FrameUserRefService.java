package com.longyou.comm.service;

import com.github.pagehelper.Page;
import com.longyou.comm.model.TFrameUserRef;
import org.cloud.exception.BusinessException;
import org.cloud.vo.FrameUserRefVO;
import org.cloud.vo.QueryParamVO;

import javax.validation.constraints.NotNull;
import java.util.List;


public interface FrameUserRefService {

    Integer create(FrameUserRefVO vo) throws BusinessException;

    Integer userCreate(FrameUserRefVO vo) throws BusinessException;

    Integer update(FrameUserRefVO vo) throws BusinessException;

    Integer userUpdate(FrameUserRefVO vo) throws BusinessException;

    List<TFrameUserRef> selectUserList(Long userId);

    int delete(Long id);

    Page<?> select(QueryParamVO queryParamVO);

    /**
     * 获取用户的属性值
     *
     * @param userId
     * @param AttributeName
     * @return
     */
    FrameUserRefVO getUserRefByAttributeName(@NotNull Long userId, @NotNull String AttributeName);
}
