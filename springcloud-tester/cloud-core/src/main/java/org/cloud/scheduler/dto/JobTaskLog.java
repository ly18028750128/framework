package org.cloud.scheduler.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobTaskLog {

    public JobTaskLog(JobExecutionContext context, String message, String result, Long execTime, String serviceName) {

        this.jobDataMap = context.getMergedJobDataMap();
        this.jobName = context.getTrigger().getJobKey().getName();
        this.group = context.getTrigger().getJobKey().getGroup();
        this.className = context.getJobDetail().getJobClass().getName();
        this.message = message;
        this.executeResult = result;
        this.result = context.getResult();
        this.serviceName = serviceName;
        this.nextFireTime = context.getNextFireTime();
        this.prevFireTime = context.getPreviousFireTime();
        this.numRefires = context.getRefireCount();
        this.fireTime = context.getFireTime();
        this.jobRunTime = execTime;
        try {
            this.scheduledFireTime = context.getScheduledFireTime();
            this.schedulerName = context.getScheduler().getSchedulerName();
        } catch (SchedulerException e) {

        }
        this.createDate = new Date();

    }

    private Date createDate;

    private String schedulerName;

    private String serviceName;

    private String message;

    private String executeResult;

    private String className;

    private String jobName;

    private String group;

    private JobDataMap jobDataMap;

    private int numRefires = 0;

    private Date fireTime;

    private Date scheduledFireTime;

    private Date prevFireTime;

    private Date nextFireTime;

    private Object result;

    private Long jobRunTime;

}
