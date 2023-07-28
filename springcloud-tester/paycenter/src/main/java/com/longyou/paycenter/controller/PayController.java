package com.longyou.paycenter.controller;

import com.alibaba.fastjson.JSON;
import com.longyou.paycenter.configuration.PayAppConfig;
import com.longyou.paycenter.configuration.PayAppConfigList;
import com.longyou.paycenter.service.PayService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cloud.utils.EnvUtil;
import org.cloud.utils.SpringContextUtil;
import org.cloud.vo.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
public class PayController {

  Logger logger = LoggerFactory.getLogger(PayController.class);

  @Autowired
  PayAppConfigList payAppConfigList;

  /**
   * 支付接口
   *
   * @param payPlatformIndex
   * @param params
   * @return
   * @throws Exception
   */

  @RequestMapping(value = "/unifiedorder/{payPlatformIndex}", method = RequestMethod.POST)
  public ResponseResult unifiedorder(@PathVariable("payPlatformIndex") Integer payPlatformIndex, @RequestBody Map<String, Object> params)
      throws Exception {
    PayAppConfig payAppConfig = payAppConfigList.getPlatformList().get(payPlatformIndex);
    PayService payService = SpringContextUtil.getBean(PayService._PAY_SERVICE_PREFIX + payAppConfig.getType(), PayService.class);
    return payService.unifiedorder(payPlatformIndex, payAppConfig, params);
  }

  /**
   * 退款
   *
   * @param payPlatformIndex
   * @param params
   * @return
   * @throws Exception
   */

  @RequestMapping(value = "/refund/{payPlatformIndex}", method = RequestMethod.POST)
  public ResponseResult refund(@PathVariable("payPlatformIndex") Integer payPlatformIndex, @RequestBody Map<String, Object> params)
      throws Exception {
    PayAppConfig payAppConfig = payAppConfigList.getPlatformList().get(payPlatformIndex);
    PayService payService = SpringContextUtil.getBean(PayService._PAY_SERVICE_PREFIX + payAppConfig.getType(), PayService.class);
    return payService.refund(payPlatformIndex, payAppConfig, params);
  }


  /**
   * 微信支付成功回调
   *
   * @param payPlatformIndex
   * @param payResult
   * @param request
   * @param response
   * @return
   * @throws Exception
   */
  @RequestMapping(value = "/receiver/{payPlatformIndex}")
  public ResponseResult receiver(@PathVariable("payPlatformIndex") int payPlatformIndex, @RequestBody Map<String, Object> payResult,
      HttpServletRequest request, HttpServletResponse response) throws Exception {
    PayAppConfig payAppConfig = payAppConfigList.getPlatformList().get(payPlatformIndex);
    PayService payService = SpringContextUtil.getBean(PayService._PAY_SERVICE_PREFIX + payAppConfig.getType(), PayService.class);
    return payService.receiver(payPlatformIndex, payAppConfig, payResult, request, response);
  }

  @RequestMapping(value = "/notify/refund/{payPlatformIndex}")
  public ResponseResult refundCallback(@PathVariable("payPlatformIndex") int payPlatformIndex,
      @RequestBody Map<String, Object> refundResult, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        PayAppConfig payAppConfig = payAppConfigList.getPlatformList().get(payPlatformIndex);
//        PayService payService = SpringContextUtil.getBean(PayService._PAY_SERVICE_PRFIX + payAppConfig.getType(), PayService.class);

    logger.info(JSON.toJSONString(refundResult));

    ResponseResult result = ResponseResult.createSuccessResult();

    result.setData(EnvUtil.single().getEnv("spring.profiles.active", "默认！"));

    return result;
  }
}

