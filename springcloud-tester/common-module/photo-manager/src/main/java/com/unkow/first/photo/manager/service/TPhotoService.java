package com.unkow.first.photo.manager.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.juna.ruiqi.api.CommonPage;
import com.unkow.first.photo.manager.mapper.TPhoto;
import java.util.List;

/**
* <p>
    *  服务类
    * </p>
*
* @author wjl
* @since 2021-07-06
*/
public interface TPhotoService extends IService<TPhoto> {

    /**
    * 按条件查询数据列表
    *
    * @param condition 条件，可以为null
    * @return List<TPhoto>
    */
    public List<TPhoto> findList(TPhoto condition);

    /**
    * 查询分页数据
    *
    * @param page      页码
    * @param limit 每页条数
    * @return CommonResult<TPhoto>
    */
    CommonPage<TPhoto> findListByPage(Integer page, Integer limit, String orderBy, QueryWrapper<TPhoto> queryWrapper);

    /**
    * 添加
    *
    * @param tPhoto 
    * @return int
    */
    boolean add(TPhoto tPhoto);

    /**
    * 删除
    *
    * @param id 主键
    * @return int
    */
    int delete(Integer id);

    /**
    * 修改
    *
    * @param tPhoto 
    * @return int
    */
    int updateData(TPhoto tPhoto);

    /**
    * id查询数据
    *
    * @param id id
    * @return TPhoto
    */
    TPhoto findById(Integer id);
}
