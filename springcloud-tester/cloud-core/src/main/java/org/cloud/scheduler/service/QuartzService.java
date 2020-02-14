package org.cloud.scheduler.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.cloud.scheduler.controller.QuartzController;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@DS("quartz")
public class QuartzService {

    final Logger logger = LoggerFactory.getLogger(QuartzService.class);


    @Autowired
    private Scheduler scheduler;

    @PostConstruct
    public void startScheduler() {
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 增加一个job
     *
     * @param jobClass     任务实现类
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     * @param jobTime      时间表达式 (这是每隔多少秒为一次任务)
     * @param jobTimes     运行的次数 （<0:表示不限次数）
     * @param jobData      参数
     */

    public void addJob(Class<? extends QuartzJobBean> jobClass, String jobName, String jobGroupName, int jobTime,
                       int jobTimes, Map jobData) throws Exception {

        // 任务名称和组构成任务key
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName)
                .build();
        // 设置job参数
        if (jobData != null && jobData.size() > 0) {
            jobDetail.getJobDataMap().putAll(jobData);
        }
        // 使用simpleTrigger规则
        Trigger trigger = null;
        if (jobTimes < 0) {
            trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName)
                    .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1).withIntervalInSeconds(jobTime))
                    .startNow().build();
        } else {
            trigger = TriggerBuilder
                    .newTrigger().withIdentity(jobName, jobGroupName).withSchedule(
                            SimpleScheduleBuilder.repeatSecondlyForever(1).withIntervalInSeconds(jobTime).withRepeatCount(jobTimes))
                    .startNow().build();
        }
        scheduler.scheduleJob(jobDetail, trigger);

    }

    /**
     * 增加一个job
     *
     * @param jobClass     任务实现类
     * @param jobName      任务名称(建议唯一)
     * @param jobGroupName 任务组名
     * @param jobTime      时间表达式 （如：0/5 * * * * ? ）
     * @param jobData      参数
     */

    public void addJob(Class<? extends QuartzJobBean> jobClass, String jobName, String jobGroupName, String jobTime, Map jobData) throws Exception {

        // 创建jobDetail实例，绑定Job实现类
        // 指明job的名称，所在组的名称，以及绑定job类
        // 任务名称和组构成任务key
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName)
                .build();
        // 设置job参数
        if (jobData != null && jobData.size() > 0) {
            jobDetail.getJobDataMap().putAll(jobData);
        }
        // 定义调度触发规则
        // 使用cornTrigger规则
        // 触发器key
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName)
                .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                .withSchedule(CronScheduleBuilder.cronSchedule(jobTime)).startNow().build();
        // 把作业和触发器注册到任务调度中
        scheduler.scheduleJob(jobDetail, trigger);

    }

    /**
     * 修改 一个job的 时间表达式
     *
     * @param jobName
     * @param jobGroupName
     * @param jobTime
     */

    public void updateJob(String jobName, String jobGroupName, String jobTime) throws Exception {

        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (!(trigger instanceof CronTrigger)) {
            throw new Exception("非按corn表达式执行的定时任务，不能更改时间表达式，如果要更改定时任务的类型请先删除后再新加！");
        }
        CronTrigger cronTrigger = (CronTrigger) trigger;
        cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(CronScheduleBuilder.cronSchedule(jobTime)).build();
        // 重启触发器
        scheduler.rescheduleJob(triggerKey, cronTrigger);
    }

    /**
     * 修改 一个job的执行间隔时间
     *
     * @param jobName
     * @param jobGroupName
     * @param jobTime
     * @param jobTimes
     * @throws Exception
     */
    public void updateJob(String jobName, String jobGroupName, int jobTime, int jobTimes) throws Exception {

        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);

        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (!(trigger instanceof SimpleTrigger)) {
            throw new Exception("非按时间间隔执行的定时任务，不能更改间隔时间，如果要更改定时任务的类型请先删除后再新加！");
        }
        SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;

        if (jobTimes < 0) {
            simpleTrigger = simpleTrigger.getTriggerBuilder().withIdentity(triggerKey).
                    withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1).withIntervalInSeconds(jobTime))
                    .build();
        } else {
            simpleTrigger = simpleTrigger.getTriggerBuilder().withIdentity(triggerKey)
                    .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(1).withIntervalInSeconds(jobTime).withRepeatCount(jobTimes))
                    .build();
        }

        // 重启触发器
        scheduler.rescheduleJob(triggerKey, simpleTrigger);
    }

    /**
     * 删除任务一个job
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组名
     */

    public void deleteJob(String jobName, String jobGroupName) throws Exception {

        scheduler.deleteJob(new JobKey(jobName, jobGroupName));

    }

    /**
     * 暂停一个job
     *
     * @param jobName
     * @param jobGroupName
     */

    public void pauseJob(String jobName, String jobGroupName) throws Exception {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复一个job
     *
     * @param jobName
     * @param jobGroupName
     */

    public void resumeJob(String jobName, String jobGroupName) throws Exception {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        scheduler.resumeJob(jobKey);
    }

    /**
     * 立即执行一个job
     *
     * @param jobName
     * @param jobGroupName
     */

    public void runAJobNow(String jobName, String jobGroupName) throws Exception {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        scheduler.triggerJob(jobKey);
    }

    /**
     * 获取所有计划中的任务列表
     *
     * @return
     */

    public List<Map<String, Object>> queryAllJob() throws Exception {
        List<Map<String, Object>> jobList = null;

        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        jobList = new ArrayList<Map<String, Object>>();
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                Map<String, Object> map = new HashMap<>();
//                map.put(QuartzController.JobFieldName.CLASSNAME.value(), );
                map.put(QuartzController.JobFieldName.JOBNAME.value(), jobKey.getName());
                map.put(QuartzController.JobFieldName.JOBGROUPNAME.value(), jobKey.getGroup());
                map.put(QuartzController.JobFieldName.DESCRIPTION.value(), "触发器[" + trigger.getKey() + "]");
                map.put(QuartzController.JobFieldName.JOBDATA.value(), trigger.getJobDataMap());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                map.put(QuartzController.JobFieldName.JOBSTATUS.value(), triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    map.put(QuartzController.JobFieldName.JOBTIME.value(), cronExpression);
                } else if (trigger instanceof SimpleTrigger) {
                    SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;
                    map.put("repeatCount", simpleTrigger.getRepeatCount());
                    map.put("repeatInterval", simpleTrigger.getRepeatInterval());
                }
                map.put("nextFireTime", trigger.getNextFireTime());
                map.put("previousFireTime", trigger.getPreviousFireTime());
//                map.put("endTime", trigger.getEndTime());


                jobList.add(map);
            }
        }
        return jobList;
    }

    /**
     * 获取所有正在运行的job
     *
     * @return
     */

    public List<Map<String, Object>> queryRunJob() throws Exception {
        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
        List<Map<String, Object>> jobList = new ArrayList<Map<String, Object>>(executingJobs.size());
        for (JobExecutionContext executingJob : executingJobs) {
            Map<String, Object> map = new HashMap<String, Object>();
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            map.put(QuartzController.JobFieldName.CLASSNAME.value(), jobDetail.getJobClass());
            map.put(QuartzController.JobFieldName.JOBNAME.value(), jobKey.getName());
            map.put(QuartzController.JobFieldName.JOBGROUPNAME.value(), jobKey.getGroup());
            map.put(QuartzController.JobFieldName.DESCRIPTION.value(), "触发器[" + trigger.getKey() + "]");
            map.put(QuartzController.JobFieldName.JOBDATA.value(), jobDetail.getJobDataMap());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            map.put(QuartzController.JobFieldName.JOBSTATUS.value(), triggerState.name());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                map.put(QuartzController.JobFieldName.JOBTIME.value(), cronExpression);
            } else if (trigger instanceof SimpleTrigger) {
                SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;
                map.put("repeatCount", simpleTrigger.getRepeatCount());
                map.put("repeatInterval", simpleTrigger.getRepeatInterval());
            }
            map.put("nextFireTime", trigger.getNextFireTime());
            map.put("previousFireTime", trigger.getPreviousFireTime());
//            map.put("endTime", trigger.getEndTime());
            jobList.add(map);
        }
        return jobList;
    }
}
