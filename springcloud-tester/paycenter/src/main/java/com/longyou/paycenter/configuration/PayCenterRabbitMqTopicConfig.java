package com.longyou.paycenter.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "system.rabbitmq.exchange")
public class PayCenterRabbitMqTopicConfig {
    List<PayCenterExchangeConfig> exchanges;

    public void setExchanges(List<PayCenterExchangeConfig> exchanges) {
        this.exchanges = exchanges;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean("rabbitmq.topicExchangeMap")
    Map<String, TopicExchange> topicExchangeMap(RabbitAdmin rabbitAdmin) {
        Map<String, TopicExchange> topicExchanges = new LinkedHashMap<>();
        for (PayCenterExchangeConfig payCenterExchangeConfig : exchanges) {
            TopicExchange topicExchange = new TopicExchange(payCenterExchangeConfig.exchangeName);
            topicExchanges.put(payCenterExchangeConfig.exchangeName, topicExchange);
            rabbitAdmin.declareExchange(topicExchange);
        }
        return topicExchanges;
    }

    @Bean("rabbitmq.topicQueuesMap")
    Map<String, List<Queue>> topicQueuesMap(RabbitAdmin rabbitAdmin) {
        Map<String, List<Queue>> queuesMap = new LinkedHashMap<>();
        for (PayCenterExchangeConfig PayCenterExchangeConfig : exchanges) {
            List<Queue> queues = new ArrayList<>();
            for (String queueName : PayCenterExchangeConfig.topicNames) {
                Queue queue = new Queue(queueName);
                queues.add(queue);
                rabbitAdmin.declareQueue(queue);
            }
            queuesMap.put(PayCenterExchangeConfig.exchangeName, queues);
        }
        return queuesMap;
    }

    @Bean("rabbitmq.bindingMap")
    Map<String, List<Binding>> bindingExchangeMessage(Map<String, List<Queue>> topicQueuesMap, Map<String, TopicExchange> topicExchanges, RabbitAdmin rabbitAdmin) {
        Map<String, List<Binding>> bindingsMap = new LinkedHashMap<>();
        for (PayCenterExchangeConfig PayCenterExchangeConfig : exchanges) {
            List<Binding> bindings = new ArrayList<>();
            for (Queue queue : topicQueuesMap.get(PayCenterExchangeConfig.exchangeName)) {
                Binding binding = BindingBuilder.bind(queue).to(topicExchanges.get(PayCenterExchangeConfig.exchangeName)).with(queue.getName());
                bindings.add(binding);
                rabbitAdmin.declareBinding(binding);
            }
            bindingsMap.put(PayCenterExchangeConfig.exchangeName, bindings);
        }
        return bindingsMap;
    }
}
