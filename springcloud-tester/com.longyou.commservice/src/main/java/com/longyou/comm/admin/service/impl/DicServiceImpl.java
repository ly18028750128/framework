package com.longyou.comm.admin.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.longyou.comm.admin.service.IDicService;
import com.longyou.comm.mapper.TSystemDicItemMapper;
import com.longyou.comm.mapper.TSystemDicMasterMapper;
import org.cloud.dimension.annotation.DataDimensionAuth;
import org.cloud.context.RequestContextManager;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.model.TSystemDicItem;
import org.cloud.model.TSystemDicMaster;
import org.cloud.dimension.utils.process.ProcessCallable;
import org.cloud.utils.process.ProcessUtil;
import org.cloud.vo.QueryParamVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static org.cloud.constant.CoreConstant._SYSTEM_DIC_CACHE_KEY;
import static org.cloud.constant.CoreConstant._SYSTEM_DIC_ITEMS_CACHE_KEY_WHIT_DOT;

@Service
public class DicServiceImpl implements IDicService {

  @Autowired
  TSystemDicMasterMapper systemDicMasterMapper;

  @Autowired
  TSystemDicItemMapper systemDicItemMapper;

  @Override
  @Transactional
  public int SaveOrUpdate(TSystemDicMaster systemDicMaster) throws Exception {

    LoginUserDetails user = RequestContextManager.single().getRequestContext().getUser();

    int updateCount = 0;
    systemDicMaster.setUpdateDate(new Date());
    systemDicMaster.setUpdateBy(user.getId());
    if (systemDicMaster.getDicMasterId() == null) {
      systemDicMaster.setCreateDate(new Date());
      systemDicMaster.setCreateBy(user.getId());
      updateCount = systemDicMasterMapper.insertSelective(systemDicMaster);
    } else {
      updateCount = systemDicMasterMapper.updateByPrimaryKeySelective(systemDicMaster);
    }

    if (CollectionUtils.isEmpty(systemDicMaster.getItems())) {
      return updateCount;
    }

    for (TSystemDicItem item : systemDicMaster.getItems()) {
      if (item.getDicMasterId() == null) {
        item.setDicMasterId(systemDicMaster.getDicMasterId());
      }
      item.setUpdateBy(user.getId());
      item.setUpdateDate(new Date());
      if (item.getDicItemId() == null) {
        item.setCreateDate(new Date());
        item.setCreateBy(user.getId());
        systemDicItemMapper.insertSelective(item);
      } else {
        systemDicItemMapper.updateByPrimaryKeySelective(item);
      }
    }

    return updateCount;
  }

  @Override
  public TSystemDicMaster getDicMasterById(Long dicMasterId) throws Exception {
    TSystemDicMaster systemDicMaster = systemDicMasterMapper.selectByPrimaryKey(dicMasterId);
    if (systemDicMaster != null) {
      systemDicMaster.setItems(systemDicItemMapper.selectByDicMasterId(dicMasterId));
    }
    return systemDicMaster;
  }

  @Override
  public TSystemDicItem getDicItemById(Long dicMasterId) throws Exception {
    return systemDicItemMapper.selectByPrimaryKey(dicMasterId);
  }

  @Override
  public List<TSystemDicItem> getDicItemsByMasterId(Long dicMasterId) throws Exception {
    return systemDicItemMapper.selectByDicMasterId(dicMasterId);
  }

  @Override
  public List<TSystemDicItem> getDicItemsByDicCode(Map<String, Object> params) throws Exception {
    Assert.notNull(params.get("dicCode"), "字典编码不能为空！");
    return systemDicItemMapper.selectByDicCode(params);
  }

  @Override
  @DataDimensionAuth(open = true)
  public Page<TSystemDicMaster> listPage(QueryParamVO queryParams) throws Exception {
    PageHelper.startPage(queryParams.getPageNum(), queryParams.getPageSize(), "status desc,dic_code asc,dic_name asc");
    return systemDicMasterMapper.listPage(queryParams);
  }

  @Autowired
  RedisUtil redisUtil;

  @Override
  @Transactional
  public void refreshCache() throws Exception {
    final Map<String, Object> params = new HashMap<>();
    params.put("status", 1);
    List<TSystemDicMaster> dicList = systemDicMasterMapper.listWithOutPaged(params);
    List<Callable<Object>> runnables = new ArrayList<>();
    redisUtil.removePattern(_SYSTEM_DIC_CACHE_KEY + "*");
    for (final TSystemDicMaster dicMaster : dicList) {
      final String hashCacheKey = _SYSTEM_DIC_CACHE_KEY + dicMaster.getBelongMicroService();
      final Map<String, Object> itemParams = new HashMap<>();
      itemParams.put("dicCode", dicMaster.getDicCode());
      itemParams.put("status", 1);
      itemParams.put("belongMicroService", dicMaster.getBelongMicroService());
      runnables.add(new ProcessCallable<Object>() {
        @Override
        public Object process() {
          final List<TSystemDicItem> dicItems = systemDicItemMapper.selectByDicCode(itemParams);
          Map<String, List<TSystemDicItem>> dicItemsMapByLanguage = dicItems.stream()
              .collect(Collectors.groupingBy(TSystemDicItem::getLanguage));
          redisUtil.hashSet(hashCacheKey, dicMaster.getDicCode() + _SYSTEM_DIC_ITEMS_CACHE_KEY_WHIT_DOT, dicItemsMapByLanguage,
              -1L);  //缓存字典项永久缓存，不过期
          redisUtil.hashSet(hashCacheKey, dicMaster.getDicCode(), dicMaster, -1L);  //缓存字典永久缓存，不过期
          return null;
        }
      });
    }
    ProcessUtil.single().runCablles(runnables, 20, 240L);
  }

}
