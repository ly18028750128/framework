package org.cloud.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "system.rabbitmq", name = "enabled", havingValue = "true")
public class RabbitMqConfig {


    @Bean
    public MessageConverter jackson2JsonMessageConverter() {
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        return jackson2JsonMessageConverter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory factory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(factory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        rabbitTemplate.setMandatory(true); //设置为true时，broker会调用basic.return方法将消息返回给生产者，从而防止消息丢失
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            log.info("消息唯一标识: {}，确认结果：{}，失败原因：{}", correlationData, ack, cause);

        });
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.info("message:{},replyCode:{},replyText:{},exchange:{},routingKey:{}", message, replyCode, replyText, exchange, routingKey);
        });
        return rabbitTemplate;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(RabbitTemplate rabbitTemplate) {
        return new RabbitAdmin(rabbitTemplate);
    }
}
