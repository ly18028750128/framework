package com.longyou.comm.service.impl;

import com.longyou.comm.mapper.TFrameUserRefMapper;
import com.longyou.comm.model.TFrameUserRef;
import com.longyou.comm.service.FrameUserRefService;
import com.longyou.comm.vo.FrameUserRefVO;
import org.cloud.exception.BusinessException;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FrameUserRefServiceImpl implements FrameUserRefService {

    @Autowired
    private TFrameUserRefMapper tFrameUserRefMapper;

    @Override
    public ResponseResult create(FrameUserRefVO vo) throws BusinessException {
        if (vo.getAttributeName() == null) {
            throw new BusinessException("属性名不能为空");
        }
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
        BeanUtils.copyProperties(vo,userRef);
        Date date = new Date();
        userRef.setUpdateDate(date);
        if (tFrameUserRefMapper.create(userRef) > 0) {
            return new ResponseResult();
        }
        throw new BusinessException("创建失败");
    }

    @Override
    public ResponseResult update(FrameUserRefVO vo) throws BusinessException {
        if(vo.getId() == null){
            throw new BusinessException("id不能为空");
        }
        TFrameUserRef tFrameUserRef = tFrameUserRefMapper.select(vo.getId());
        if (tFrameUserRef == null) {
            throw new BusinessException("要修改的id不存在");
        }
        int count = tFrameUserRefMapper.nameCount(vo.getAttributeName());
        System.out.println(count);
        if (count >= 1){
            throw new BusinessException("属性名不能重复");
        }
        TFrameUserRef userRef = new TFrameUserRef();
        userRef.setId(vo.getId());
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
}