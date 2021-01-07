package com.longyou.paycenter.configuration;

import lombok.Data;

//type: weixin-microapp
//app-name: weixin-xgsixteen
//appid: wx48d43e91a95c5fc2
//mch-id: 1554920771  # 商户号
//mch-pay-password: Huangtushengtai20190708huangtuly
@Data
public class PayAppConfig {
    private String type;
    private String appName;
    private String appid;
    private String mchId;
    private String mchPayPassword;
    private String callbackUrl;
    private String refundCallbackUrl;

    //    topic-exchange: ${rabbitmq.exchange.exchanges[1].exchange-name} # 消息队列exchange
//    pay-topic-name: ${rabbitmq.exchange.exchanges[1].topic-names[0]} # 支付topic
//    refund-topic-name: ${rabbitmq.exchange.exchanges[1].topic-names[1]} # 支付topic
    private String topicExchange;
    private String payTopicName;
    private String refundTopicName;
    private String trustCertsPath;

}
