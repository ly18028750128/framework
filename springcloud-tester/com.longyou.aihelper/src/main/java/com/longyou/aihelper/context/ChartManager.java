package com.longyou.aihelper.context;


import com.longyou.aihelper.aiml.service.impl.AskToAIML;
import com.longyou.aihelper.aiml.service.impl.AskToDB;
import org.cloud.utils.SpringContextUtil;

public class ChartManager {

  private static AskToAIML askToAIML;
  private static AskToDB askToDB = null;
  private static ChartContext chartContext = null;
  private static ChartManager instance = null;

  /**
   * 单例模式
   */
  private ChartManager() {
    chartContext = new ChartContext(askToAIML, askToDB);
  }

  public static synchronized ChartManager getInstance() {

    if (askToDB == null) {
      askToDB = SpringContextUtil.getBean("aiml.askToDB");
    }

    if (askToAIML == null) {
      askToAIML = SpringContextUtil.getBean("aiml.askToAIML");
    }

    if (instance == null) {
      instance = new ChartManager();
    }
    return instance;
  }

  public String response(String input) throws Exception {
    return chartContext.response(input);
  }

}
