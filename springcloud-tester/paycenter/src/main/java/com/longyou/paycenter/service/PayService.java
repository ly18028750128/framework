package com.longyou.paycenter.service;

import com.longyou.paycenter.configuration.PayAppConfig;
import org.cloud.vo.ResponseResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

// 支付平台对接
public interface PayService {

    final static String _PAY_SERVICE_PRFIX = "pay.platform.service.";

    // 统一下单服务
    ResponseResult unifiedorder(final Integer payPlatformIndex, final PayAppConfig payAppConfig, final Map<String, Object> params) throws Exception;

    // 退款服务
    ResponseResult refund(final Integer payPlatformIndex, final PayAppConfig payAppConfig, final Map<String, Object> params) throws Exception;


    // 接收地址
    ResponseResult receiver(final Integer payPlatformIndex, final PayAppConfig payAppConfig, Map<String, Object> payResult, HttpServletRequest request, HttpServletResponse response) throws Exception;



}
