package com.longyou.aihelper.scheduler;

import com.longyou.aihelper.lucene.AimlIndexService;
import lombok.extern.slf4j.Slf4j;
import org.cloud.scheduler.job.BaseQuartzJobBean;
import org.cloud.utils.SpringContextUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AimlIndexRefreshJob extends BaseQuartzJobBean {

  @Override
  protected void init() {
    this.jobName = "定时aiml的lucene的索引";
    jobData.put("description", "定时aiml的lucene的索引！");
    this.jobTime = "0 0/10 * * * ? *";  //10分钟刷新一次
  }

  @Override
  protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    // todo 需要做增量，暂时不实现
    try {
      final AimlIndexService aimlIndexService = SpringContextUtil.getBean(AimlIndexService.class);
      if (aimlIndexService != null) {
        aimlIndexService.createOrUpdateIndex();
      }
    } catch (Exception e) {
      log.error("{}", e);
    }
  }
}
