package com.longyou.rabbitmq.test;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = {"testExchangeQueue2", "testExchangeQueue1"})
@Slf4j
public class ReceiverTest {

  @RabbitHandler
  public void process(Object results, Channel channel, @Header(name = "amqp_deliveryTag") long deliveryTag,
      @Header("amqp_redelivered") boolean redelivered) throws Exception {
    log.info("ReceiverTest:{}", results);
    channel.basicAck(deliveryTag,false);
  }
}
