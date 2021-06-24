package com.longyou.paycenter.mapper;

import com.longyou.paycenter.model.PayMerchant;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PayMerchantMapper {

  /**
   * delete by primary key
   *
   * @param payMerchantId primaryKey
   * @return deleteCount
   */
  int deleteByPrimaryKey(Long payMerchantId);

  /**
   * insert record to table
   *
   * @param record the record
   * @return insert count
   */
  int insert(PayMerchant record);

  int insertOrUpdate(PayMerchant record);

  int insertOrUpdateSelective(PayMerchant record);

  /**
   * insert record to table selective
   *
   * @param record the record
   * @return insert count
   */
  int insertSelective(PayMerchant record);

  /**
   * select by primary key
   *
   * @param payMerchantId primary key
   * @return object by primary key
   */
  PayMerchant selectByPrimaryKey(Long payMerchantId);

  /**
   * update record selective
   *
   * @param record the updated record
   * @return update count
   */
  int updateByPrimaryKeySelective(PayMerchant record);

  /**
   * update record
   *
   * @param record the updated record
   * @return update count
   */
  int updateByPrimaryKey(PayMerchant record);

  int updateBatch(List<PayMerchant> list);

  int batchInsert(@Param("list") List<PayMerchant> list);

  int updateBatchSelective(List<PayMerchant> list);
}