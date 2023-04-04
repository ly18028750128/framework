package com.longyou.comm.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.longyou.comm.mapper.TFrameUserRefMapper;
import com.longyou.comm.model.TFrameUserRef;
import com.longyou.comm.service.FrameUserRefService;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;
import org.cloud.exception.BusinessException;
import org.cloud.utils.CollectionUtil;
import org.cloud.vo.FrameUserRefVO;
import org.cloud.vo.QueryParamVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

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
        // todo 暂时这样处理，如果已经有属性存在的话，那么直接返回0,并将查询到的vo通过参数返回给调用类
        FrameUserRefVO frameUserRefVOResult = this.getUserRefByAttributeName(vo.getUserId(), vo.getAttributeName());
        if (CollectionUtil.single().isNotEmpty(frameUserRefVOResult)) {
            BeanUtils.copyProperties(frameUserRefVOResult, vo);
            return 0;
        }
        if (vo.getUserId() == null) {
            throw new BusinessException("用户id不能为空");
        }
        checkInVo(vo);
        TFrameUserRef userRef = new TFrameUserRef();
        BeanUtils.copyProperties(vo, userRef, new String[]{"createDate", "updateDate"});
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
    public Integer userCreate(FrameUserRefVO vo) throws BusinessException {
        LoginUserDetails loginUserDetails = RequestContextManager.single().getRequestContext().getUser();
        vo.setUserId(loginUserDetails.getId());
        return this.update(vo);
    }


    /**
     * 修改
     *
     * @param vo
     * @return
     * @throws BusinessException
     */
    @Override
    public Integer update(FrameUserRefVO vo) throws BusinessException {
        TFrameUserRef userRef = new TFrameUserRef();
        BeanUtils.copyProperties(vo, userRef, new String[]{"createDate", "updateDate"});
        return tFrameUserRefMapper.update(userRef);
    }

    @Override
    public Integer userUpdate(FrameUserRefVO vo) throws BusinessException {
        LoginUserDetails loginUserDetails = RequestContextManager.single().getRequestContext().getUser();
        vo.setUserId(loginUserDetails.getId());
        return this.update(vo);
    }

    @Override
    public List<TFrameUserRef> selectUserList(Long userId) {
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

    @Override
    public FrameUserRefVO getUserRefByAttributeName(@NotNull Long userId, @NotNull String AttributeName) {
        TFrameUserRef tFrameUserRef = tFrameUserRefMapper.selectUserRefByAttributeName(userId, AttributeName);
        if (tFrameUserRef == null) {
            return null;
        }
        FrameUserRefVO frameUserRefVO = new FrameUserRefVO();
        BeanUtils.copyProperties(tFrameUserRef, frameUserRefVO);
        return frameUserRefVO;
    }


    private void checkInVo(FrameUserRefVO vo) throws BusinessException {
        if (vo.getAttributeValue() == null) {
            throw new BusinessException("属性值不能为空");
        }
//        if (vo.getCreateBy() == null) {
//            throw new BusinessException("创建人不能为空");
//        }
//        if (vo.getUpdateBy() == null) {
//            throw new BusinessException("更新人不能为空");
//        }
    }

}
