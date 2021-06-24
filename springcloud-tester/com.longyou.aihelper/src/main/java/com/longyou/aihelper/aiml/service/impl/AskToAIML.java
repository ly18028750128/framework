package com.longyou.aihelper.aiml.service.impl;

import bitoflife.chatterbean.AliceBot;
import bitoflife.chatterbean.AliceBotMother;
import com.longyou.aihelper.aiml.service.IAskApproach;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AskToAIML implements IAskApproach {

  private AliceBot bot = null;

  public AskToAIML(final String configPath, final String corpusPath) {
    AliceBotMother mother = new AliceBotMother();
    mother.setUp();
    try {
      bot = mother.newInstance(configPath, corpusPath);
    } catch (Exception e) {
      log.error("{}", e);
    }
  }

  public String response(String input) {
    return bot.respond(input);
  }
}
