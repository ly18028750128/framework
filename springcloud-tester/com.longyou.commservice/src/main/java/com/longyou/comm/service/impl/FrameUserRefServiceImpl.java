package com.longyou.comm.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.longyou.comm.mapper.TFrameUserRefMapper;
import com.longyou.comm.model.TFrameUserRef;
import com.longyou.comm.service.FrameUserRefService;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;
import org.cloud.exception.BusinessException;
import org.cloud.vo.FrameUserRefVO;
import org.cloud.vo.QueryParamVO;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FrameUserRefServiceImpl implements FrameUserRefService {

    @Autowired
    private TFrameUserRefMapper tFrameUserRefMapper;

    /**
     * 添加
     *
     * @param vo
     * @return
     * @throws BusinessException
     */
    @Override
    public Integer create(FrameUserRefVO vo) throws BusinessException {

        if (vo.getUserId() == null) {
            throw new BusinessException("用户id不能为空");
        }
        if (vo.getAttributeValue() == null) {
            throw new BusinessException("属性值不能为空");
        }
        if (vo.getCreateBy() == null) {
            throw new BusinessException("创建通过不能为空");
        }
        if (vo.getUpdateBy() == null) {
            throw new BusinessException("更新通过不能为空");
        }
        TFrameUserRef userRef = new TFrameUserRef();
        BeanUtils.copyProperties(vo, userRef);
        Date date = new Date();
        userRef.setUpdateDate(date);
        return tFrameUserRefMapper.create(userRef);
    }

    /**
     * 根据当前登陆用户添加
     *
     * @param vo
     * @return
     * @throws BusinessException
     */
    @Override
    public ResponseResult userCreate(FrameUserRefVO vo) throws BusinessException {
        LoginUserDetails loginUserDetails = RequestContextManager.single().getRequestContext().getUser();

        if (loginUserDetails.getId() == null) {
            throw new BusinessException("用户id不能为空");
        }
        if (vo.getAttributeValue() == null) {
            throw new BusinessException("属性值不能为空");
        }
        if (vo.getCreateBy() == null) {
            throw new BusinessException("创建通过不能为空");
        }
        if (vo.getUpdateBy() == null) {
            throw new BusinessException("更新通过不能为空");
        }
        TFrameUserRef userRef = new TFrameUserRef();
        BeanUtils.copyProperties(vo, userRef);
        userRef.setUserId(loginUserDetails.getId());
        Date date = new Date();
        userRef.setUpdateDate(date);
        if (tFrameUserRefMapper.create(userRef) > 0) {
            return new ResponseResult();
        }
        throw new BusinessException("创建失败");
    }


    /**
     * 修改
     *
     * @param vo
     * @return
     * @throws BusinessException
     */
    @Override
    public ResponseResult update(FrameUserRefVO vo) throws BusinessException {
        TFrameUserRef tFrameUserRef = tFrameUserRefMapper.selectUserRefList(vo.getUserId());
        if (tFrameUserRef == null) {
            throw new BusinessException("要修改的id不存在");
        }

        TFrameUserRef userRef = new TFrameUserRef();
        userRef.setId(vo.getId());
        userRef.setUserId(vo.getUserId());
        userRef.setAttributeName(vo.getAttributeName());
        userRef.setAttributeValue(vo.getAttributeValue());
        userRef.setCreateBy(vo.getCreateBy());
        userRef.setRemark(vo.getRemark());
        userRef.setUpdateBy(vo.getUpdateBy());
        Date date = new Date();
        userRef.setUpdateDate(date);
        if (tFrameUserRefMapper.update(userRef) > 0) {
            return new ResponseResult();
        }
        throw new BusinessException("修改失败");
    }

    @Override
    public ResponseResult userUpdate(FrameUserRefVO vo) throws BusinessException {
        LoginUserDetails loginUserDetails = RequestContextManager.single().getRequestContext().getUser();
        if (loginUserDetails.getId() == null) {
            throw new BusinessException("id不能为空");
        }
        TFrameUserRef tFrameUserRef = tFrameUserRefMapper.selectUserRefList(loginUserDetails.getId());
        if (tFrameUserRef == null) {
            throw new BusinessException("要修改的id不存在");
        }

        TFrameUserRef userRef = new TFrameUserRef();
        userRef.setUserId(loginUserDetails.getId());
        userRef.setAttributeName(vo.getAttributeName());
        userRef.setAttributeValue(vo.getAttributeValue());
        userRef.setCreateBy(vo.getCreateBy());
        userRef.setRemark(vo.getRemark());
        userRef.setUpdateBy(vo.getUpdateBy());
        Date date = new Date();
        userRef.setUpdateDate(date);
        if (tFrameUserRefMapper.update(userRef) > 0) {
            return new ResponseResult();
        }
        throw new BusinessException("修改失败");
    }

    @Override
    public TFrameUserRef selectUserList(Long userId) {
        return tFrameUserRefMapper.selectUserRefList(userId);
    }

    @Override
    public int delete(Long id) {
        return tFrameUserRefMapper.delete(id);
    }

    @Override
    public Page<TFrameUserRef> select(QueryParamVO queryParamVO) {
        PageHelper.startPage(queryParamVO.getPageNum(), queryParamVO.getPageSize());
        return tFrameUserRefMapper.select(queryParamVO);
    }
}
