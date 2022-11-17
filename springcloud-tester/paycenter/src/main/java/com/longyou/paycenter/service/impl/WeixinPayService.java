package com.longyou.paycenter.service.impl;


import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.longyou.paycenter.configuration.PayAppConfig;
import com.longyou.paycenter.constant.WeixinPayConstants;
import com.longyou.paycenter.service.PayService;
import okhttp3.*;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;
import org.cloud.exception.BusinessException;
import org.cloud.utils.MapUtil;
import org.cloud.utils.http.HttpRequestParams;
import org.cloud.utils.http.OKHttpClientBuilder;
import org.cloud.utils.http.OKHttpClientUtil;
import org.cloud.vo.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Service(PayService._PAY_SERVICE_PREFIX + "weixin-microapp")
@SystemResource(path="微信支付")
public class WeixinPayService implements PayService {

    Logger logger= LoggerFactory.getLogger(WeixinPayService.class);

    @Override
    public ResponseResult unifiedorder(final Integer payPlatformIndex, PayAppConfig payAppConfig, Map<String, Object> params) throws Exception {
        Long currentStart = System.currentTimeMillis();
        ResponseResult responseResult = ResponseResult.createSuccessResult();
//        final String unifiedorderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        params.put(WeixinPayConstants.ColumnUnifiedorder.APPID.value(), payAppConfig.getAppid());
        params.put(WeixinPayConstants.ColumnUnifiedorder.MCH_ID.value(), payAppConfig.getMchId());
        params.put(WeixinPayConstants.ColumnUnifiedorder.DEVICE_INFO.value(), payAppConfig.getAppName());
        params.put(WeixinPayConstants.ColumnUnifiedorder.NONCE_STR.value(), WXPayUtil.generateNonceStr());
        params.put(WeixinPayConstants.ColumnUnifiedorder.SPBILL_CREATE_IP.value(), InetAddress.getLocalHost().getHostAddress());

        params.put(WeixinPayConstants.ColumnUnifiedorder.TIME_START.value(), Long.toString(currentStart));
//        params.put(WeixinPayConstants.ColumnUnifiedorder.TIME_EXPIRE.value(), Long.toString(currentStart+ 5*60*1000));
        // 接收地址要写到配置文件里，暂时先测试
        params.put(WeixinPayConstants.ColumnUnifiedorder.NOTIFY_URL.value(), payAppConfig.getCallbackUrl() + payPlatformIndex);
        params.put(WeixinPayConstants.ColumnUnifiedorder.TRADE_TYPE.value(), "JSAPI");
        LoginUserDetails user = RequestContextManager.single().getRequestContext().getUser();
        params.put(WeixinPayConstants.ColumnUnifiedorder.OPENID.value(), user.getUsername());
        final Map<String, String> mapStr = MapUtil.single().toStringMap(params);
//        final String sign =WXPayUtil.generateSignature(mapStr,payAppConfig.getMchPayPassword());
        HttpRequestParams httpRequestParams = new HttpRequestParams();
        httpRequestParams.setRequestBodyStr(WXPayUtil.generateSignedXml(mapStr, payAppConfig.getMchPayPassword()));
        Call call = OKHttpClientUtil.single().createPostCall(httpRequestParams, WXPayConstants.UNIFIEDORDER_URL, MediaType.parse("application/xml"));
        ResponseBody body = call.execute().body();
        String bodyStr = body.string();
        body.close();

        final Map<String, String> weixinReturnResult = WXPayUtil.xmlToMap(bodyStr);
        if (!"SUCCESS".equalsIgnoreCase(weixinReturnResult.get("result_code"))) {
            throw new BusinessException("微信支付失败",weixinReturnResult, HttpStatus.BAD_REQUEST.value());
        } else {
            Map<String, String> returnData = new LinkedHashMap<>();
            returnData.put("appId", payAppConfig.getAppid());
            returnData.put("timeStamp", Long.toString(currentStart));
            returnData.put("nonceStr", weixinReturnResult.get("nonce_str"));
            returnData.put("package", "prepay_id=" + weixinReturnResult.get("prepay_id"));
            returnData.put("signType", "MD5");
            final String paySign = WXPayUtil.generateSignature(returnData, payAppConfig.getMchPayPassword());
            returnData.put("paySign", paySign);
            responseResult.setData(returnData);
        }
        return responseResult;
    }

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    @SystemResource(index = 1 , value = "微信退款",description = "微信退款程序，仅财务人员可以用",authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    public ResponseResult refund(Integer payPlatformIndex, PayAppConfig payAppConfig, Map<String, Object> params) throws Exception {

        ResponseResult responseResult = ResponseResult.createSuccessResult();
        params.put(WeixinPayConstants.ColumnRefund.APPID.value(), payAppConfig.getAppid());
        params.put(WeixinPayConstants.ColumnRefund.MCH_ID.value(), payAppConfig.getMchId());
        params.put(WeixinPayConstants.ColumnRefund.NONCE_STR.value(), WXPayUtil.generateNonceStr());
        params.put(WeixinPayConstants.ColumnRefund.NOTIFY_URL.value(),payAppConfig.getRefundCallbackUrl()+payPlatformIndex);
        final Map<String, String> mapStr = MapUtil.single().toStringMap(params);
        OkHttpClient.Builder builder = OKHttpClientBuilder.single().buildOKHttpClient("PKCS12", payAppConfig.getTrustCertsPath(), payAppConfig.getMchId());
        builder = builder.connectionSpecs(Arrays.asList(
                ConnectionSpec.MODERN_TLS,
                ConnectionSpec.COMPATIBLE_TLS,
                ConnectionSpec.CLEARTEXT,
                ConnectionSpec.RESTRICTED_TLS));
        OkHttpClient httpClient = builder.build();
        Request.Builder request = new Request.Builder();
        request.url(HttpUrl.parse(WXPayConstants.REFUND_URL).newBuilder().build());
        request.post(RequestBody.create(WXPayUtil.generateSignedXml(mapStr, payAppConfig.getMchPayPassword()), MediaType.parse("application/xml")));
        ResponseBody body = httpClient.newCall(request.build()).execute().body();

        String bodyStr = body.string();
        body.close();
        final Map<String,String> weixinReturnResult = WXPayUtil.xmlToMap(bodyStr);
        responseResult.setData(weixinReturnResult);

        if (!"SUCCESS".equalsIgnoreCase(weixinReturnResult.get("result_code"))) {
            responseResult = ResponseResult.createFailResult();
            responseResult.setData(weixinReturnResult);
        }else{
            // 发送退款成功消息
            rabbitTemplate.convertAndSend(payAppConfig.getTopicExchange(), payAppConfig.getRefundTopicName(), responseResult);
        }

        return responseResult;
    }


    public ResponseResult receiver(final Integer payPlatformIndex, PayAppConfig payAppConfig, Map<String, Object> payResult, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.put("messageId", String.valueOf(UUID.randomUUID()));
        responseResult.put("createTime", System.currentTimeMillis());
        responseResult.setMessage(payAppConfig.getAppName() + ":" + payAppConfig.getAppid() + ":" + payAppConfig.getMchId() + ":微信支付返回！");
        responseResult.setData(payResult);
        rabbitTemplate.convertAndSend(payAppConfig.getTopicExchange(), payAppConfig.getPayTopicName(), responseResult);
        logger.info("微信支付回调返回结果："+ JSON.toJSONString(responseResult) );
        return responseResult;
    }
}
