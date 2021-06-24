package com.longyou.paycenter.controller;

import com.longyou.paycenter.model.PayMerchant;
import com.longyou.paycenter.service.PayMerchantService;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant.AuthMethod;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商户管理controller
 */
@RestController
@RequestMapping("/payMerchant")
@SystemResource(path = "/payMerchant", description = "商户管理")
public class PayMerchantController {

  @Autowired
  PayMerchantService payMerchantService;

  /**
   * 增加
   *
   * @param payMerchant
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/add", method = RequestMethod.POST)
  @SystemResource(value = "addMerchant", description = "增加商户", authMethod = AuthMethod.ALLSYSTEMUSER)
  public ResponseResult addMerchant(@RequestBody final PayMerchant payMerchant) throws Exception {
    final ResponseResult result = ResponseResult.createSuccessResult();
    result.setData(payMerchantService.insertSelective(payMerchant));
    return result;
  }



}
