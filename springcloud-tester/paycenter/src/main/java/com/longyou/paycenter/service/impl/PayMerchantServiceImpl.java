package com.longyou.paycenter.service.impl;

import com.longyou.paycenter.mapper.PayMerchantMapper;
import com.longyou.paycenter.model.PayMerchant;
import com.longyou.paycenter.service.PayMerchantService;
import java.util.List;
import org.cloud.encdec.service.AESService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayMerchantServiceImpl implements PayMerchantService {


  @Autowired
  PayMerchantMapper payMerchantMapper;

  @Autowired
  AESService aesService;

  @Override
  public int deleteByPrimaryKey(Long payMerchantId) {
    return payMerchantMapper.deleteByPrimaryKey(payMerchantId);
  }

  @Override
  public int insert(PayMerchant payMerchant) throws Exception {
    return payMerchantMapper.insert(payMerchant);
  }

  @Override
  public int insertOrUpdate(PayMerchant record) {
    return payMerchantMapper.insertOrUpdate(record);
  }

  @Override
  public int insertOrUpdateSelective(PayMerchant record) {
    return payMerchantMapper.insertOrUpdateSelective(record);
  }

  @Override
  public int insertSelective(PayMerchant payMerchant) throws Exception {
    payMerchant.validate();
    encryptAesKeyAndViVal(payMerchant);
    return payMerchantMapper.insertSelective(payMerchant);
  }

  public void encryptAesKeyAndViVal(PayMerchant payMerchant) throws Exception {
    payMerchant.setAesKey(aesService.encrypt(payMerchant.getAesKey()));
    payMerchant.setAesViVal(aesService.encrypt(payMerchant.getAesViVal()));
  }

  @Override
  public PayMerchant selectByPrimaryKey(Long payMerchantId) {
    return payMerchantMapper.selectByPrimaryKey(payMerchantId);
  }

  @Override
  public int updateByPrimaryKeySelective(PayMerchant payMerchant) throws Exception {
    payMerchant.validate();
    encryptAesKeyAndViVal(payMerchant);
    return payMerchantMapper.updateByPrimaryKeySelective(payMerchant);
  }

  @Override
  public int updateByPrimaryKey(PayMerchant record) {
    return payMerchantMapper.updateByPrimaryKey(record);
  }

  @Override
  public int updateBatch(List<PayMerchant> list) {
    return payMerchantMapper.updateBatch(list);
  }

  @Override
  public int updateBatchSelective(List<PayMerchant> list) {
    return payMerchantMapper.updateBatchSelective(list);
  }

  @Override
  public int batchInsert(List<PayMerchant> list) {
    return payMerchantMapper.batchInsert(list);
  }
}


