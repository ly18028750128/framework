package com.longyou.paycenter.configuration;

import java.util.List;

public class PayCenterExchangeConfig{
    String exchangeName;
    List<String> topicNames;
    public void setTopicNames(List<String> topicNames) {
        this.topicNames = topicNames;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }
}
