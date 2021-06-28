package com.longyou.aihelper.aiml.service.impl;

import com.longyou.aihelper.aiml.service.IAskApproach;
import com.longyou.aihelper.lucene.AimlIndexService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("aiml.askToDB")
@Slf4j
public class AskToDB implements IAskApproach {

  @Autowired
  private AimlIndexService aimlIndexService;

  public String response(String input) {
    List<String> list = null;
    try {
      list = aimlIndexService.queryReplayList("copyfield", input);
    } catch (Exception e) {
      log.error("{}", e);
    }

    if (list.size() > 0) {
      return list.get(0);
    } else {
      return "";
    }
  }
}
