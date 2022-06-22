package com.unkow.first.photo.manager.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.juna.ruiqi.api.CommonPage;
import com.unkow.first.photo.manager.mapper.TPhoto;
import com.unkow.first.photo.manager.mapper.TPhotoMapper;
import com.unkow.first.photo.manager.service.TPhotoService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wjl
 * @since 2021-07-06
 */
@Service
public class TPhotoServiceImpl extends ServiceImpl<TPhotoMapper, TPhoto> implements TPhotoService {

    @Override
    public List<TPhoto> findList(TPhoto condition) {
        QueryWrapper<TPhoto> query = Wrappers.query();
        if (condition != null) {
            query.setEntity(condition);
        }
        return getBaseMapper().selectList(query);
    }

    @Override
    public CommonPage<TPhoto> findListByPage(Integer page, Integer limit, String orderBy, QueryWrapper<TPhoto> queryWrapper) {
        if (StringUtils.isEmpty(orderBy)) {
            PageHelper.startPage(page, limit);
        } else {
            PageHelper.startPage(page, limit, orderBy);
        }
        List<TPhoto> list = getBaseMapper().selectList(queryWrapper);
        return CommonPage.restPage(list);
    }

    @Override
    public boolean add(TPhoto tPhoto) {
        if (getBaseMapper().insert(tPhoto) > 0) {
            return true;
        }
        return false;
    }

    @Override
    public int delete(Integer id) {
        return getBaseMapper().deleteById(id);
    }

    @Override
    public int updateData(TPhoto tPhoto) {
        return getBaseMapper().updateById(tPhoto);
    }

    @Override
    public TPhoto findById(Integer id) {
        return getBaseMapper().selectById(id);
    }
}
