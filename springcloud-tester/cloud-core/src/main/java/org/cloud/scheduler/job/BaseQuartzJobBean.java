package org.cloud.scheduler.job;

import org.springframework.scheduling.quartz.QuartzJobBean;

public abstract class BaseQuartzJobBean extends QuartzJobBean {
    String jobName;
    String groupName;
}
