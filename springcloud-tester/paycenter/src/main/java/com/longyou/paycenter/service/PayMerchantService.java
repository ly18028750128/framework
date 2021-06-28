package com.longyou.paycenter.service;

import com.longyou.paycenter.model.PayMerchant;import java.util.List;

public interface PayMerchantService {


  int deleteByPrimaryKey(Long payMerchantId);

  int insert(PayMerchant record) throws Exception;

  int insertOrUpdate(PayMerchant record);

  int insertOrUpdateSelective(PayMerchant record);

  int insertSelective(PayMerchant record) throws Exception;

  PayMerchant selectByPrimaryKey(Long payMerchantId);

  int updateByPrimaryKeySelective(PayMerchant record) throws Exception;

  int updateByPrimaryKey(PayMerchant record);

  int updateBatch(List<PayMerchant> list);

  int updateBatchSelective(List<PayMerchant> list);

  int batchInsert(List<PayMerchant> list);
}


