package com.longyou.aihelper.config;

import com.longyou.aihelper.aiml.service.impl.AskToAIML;
import com.longyou.aihelper.aiml.service.impl.GossipLoad;
import java.io.IOException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.cloud.exception.BusinessException;
import org.cloud.scheduler.job.BaseQuartzJobBean;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "system.aiml")
@Setter
@Slf4j
public class AimlConfig {

  private String gossipPath;  // 对话内容
  private String destination; // 对话内容
  private String configPath;  // AliceBotMother 配置文件
  private String corpusPath;  // AliceBotMother 语言包路径


  @Bean
  public GossipLoad gossipLoad() throws BusinessException {
    GossipLoad gossipLoad = new GossipLoad();
    try {
      gossipLoad.load(gossipPath, destination);
    } catch (IOException e) {
      log.error("{}", e);
    } finally {
      gossipLoad.clean();
    }
    return gossipLoad;
  }

  @Bean
  public AskToAIML askToAIML() throws Exception {
    AskToAIML askToAIML = new AskToAIML(configPath, corpusPath);
    return askToAIML;
  }

  @Bean
  public AimlIndexJob aimlIndexJob(GossipLoad gossipLoad) {
    return new AimlIndexJob(gossipLoad);
  }

  class AimlIndexJob extends BaseQuartzJobBean {

    private GossipLoad gossipLoad;

    public AimlIndexJob(GossipLoad gossipLoad) {
      this.gossipLoad = gossipLoad;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
      try {
        gossipLoad.load(gossipPath, destination);
      } catch (IOException | BusinessException e) {
        log.error("{}", e);
      } finally {
        gossipLoad.clean();
      }
    }

    @Override
    protected void init() {
      this.jobName = "定时aiml的gossipPath的到目标xml文件";
      jobData.put("description", "定时aiml的gossipPath的到目标xml文件");
      this.jobTime = "0 0/10 * * * ? *";  //10分钟刷新一次
    }
  }

}
