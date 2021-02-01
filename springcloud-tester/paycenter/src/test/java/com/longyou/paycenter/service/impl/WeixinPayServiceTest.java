package com.longyou.paycenter.service.impl;

import com.longyou.paycenter.starter.PayCenterApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PayCenterApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Slf4j
class RabbitTemplateTest {


  @Autowired
  RabbitTemplate rabbitTemplate;

  @Test
  void send() throws Exception {
    // testExchangeQueue1 routingKey 为#.testExchange.#
    // testExchangeQueue2 routingKey 为*.testExchange.*
    rabbitTemplate.convertAndSend("testExchange", ".testExchange.", "测试消息1");
    rabbitTemplate.convertAndSend("testExchange", "message.2.testExchange.message.2", "测试消息2");
    rabbitTemplate.convertAndSend("testExchange", "3.testExchange.3", "测试消息3");
//    Thread.sleep(1L);
    for (int i = 0; i < 2; i++) {
      log.info(i + "::::testExchangeQueue1 message is::::{}", rabbitTemplate.receiveAndConvert("testExchangeQueue1", 1000L));
      log.info(i + "::::testExchangeQueue2 message is::::{}", rabbitTemplate.receiveAndConvert("testExchangeQueue2", 1000L));
    }

    log.info(3 + "::::testExchangeQueue1 message is::::{}", rabbitTemplate.receiveAndConvert("testExchangeQueue1", 1000L));

    Assert.assertNull(rabbitTemplate.receiveAndConvert("testExchangeQueue2", 1000L));
  }
}