package org.cloud.scheduler.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.cloud.scheduler.controller.QuartzController;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
@DS("quartz")
@Lazy
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
                       int jobTimes, Map<String, ?> jobData, Boolean isStartNow) throws Exception {

        // 任务名称和组构成任务key
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
        // 使用simpleTrigger规则
        Trigger trigger = null;
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.repeatSecondlyForever(1).withIntervalInSeconds(jobTime);

        // 重复次数大于零
        if (jobTimes > 0) {
            scheduleBuilder.withRepeatCount(jobTimes);
        }

        TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName)
                .withDescription(getDescription(jobData)).withSchedule(scheduleBuilder);

        trigger = triggerBuilder.build();

        // 设置job参数
        if (jobData != null && jobData.size() > 0) {
            trigger.getJobDataMap().putAll(jobData);
            jobDetail.getJobDataMap().putAll(jobData);
        }

        scheduler.scheduleJob(jobDetail, trigger);
//        // 是否增加时就启动,时间间隔模式，默认第一次就是新建时就启动，所以这里不用设置
//        if (isStartNow) {
//            this.runAJobNow(jobName, jobGroupName);
//        }
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
    public void addJob(Class<? extends QuartzJobBean> jobClass, String jobName,
                       String jobGroupName, String jobTime, Map<String, ?> jobData,
                       Boolean isStartNow) throws Exception {

        // 创建jobDetail实例，绑定Job实现类
        // 指明job的名称，所在组的名称，以及绑定job类
        // 任务名称和组构成任务key
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
        // 定义调度触发规则
        // 使用cornTrigger规则
        // 触发器key
        TriggerBuilder triggerBuilder = TriggerBuilder.newTrigger().withIdentity(jobName, jobGroupName)
                .withDescription(getDescription(jobData)).startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                .withSchedule(CronScheduleBuilder.cronSchedule(jobTime));

        Trigger trigger = triggerBuilder.build();

        // 设置job参数
        if (jobData != null && jobData.size() > 0) {
            trigger.getJobDataMap().putAll(jobData);
            jobDetail.getJobDataMap().putAll(jobData);
        }

        // 把作业和触发器注册到任务调度中
        scheduler.scheduleJob(jobDetail, trigger);

        // 是否增加时就启动
        if (isStartNow) {
            this.runAJobNow(jobName, jobGroupName, trigger.getJobDataMap());
        }
    }

    /**
     * 修改 一个job的 时间表达式
     *
     * @param jobName
     * @param jobGroupName
     * @param jobTime
     */
    public void updateJob(String jobName, String jobGroupName, String jobTime, Map<String, ?> jobData,
                          Boolean isStartNow) throws Exception {

        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (!(trigger instanceof CronTrigger)) {
            throw new Exception("非按corn表达式执行的定时任务，不能更改时间表达式，如果要更改定时任务的类型请先删除后再新加！");
        }
        CronTrigger cronTrigger = (CronTrigger) trigger;
        TriggerBuilder<CronTrigger> triggerBuilder = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).
                withDescription(getDescription(jobData)).withSchedule(CronScheduleBuilder.cronSchedule(jobTime));

        cronTrigger = triggerBuilder.build();
        JobDetail jobDetail = scheduler.getJobDetail(cronTrigger.getJobKey());
        cronTrigger.getJobDataMap().clear();
        jobDetail.getJobDataMap().clear();
        if (jobData != null) {
            cronTrigger.getJobDataMap().putAll(jobData);
            jobDetail.getJobDataMap().putAll(jobData);
        }

        // 重启触发器
//        scheduler.rescheduleJob(triggerKey, cronTrigger);
        this.deleteJob(jobName, jobGroupName);
        scheduler.scheduleJob(jobDetail, cronTrigger);
        // 是否增加时就启动
        if (isStartNow) {
            this.runAJobNow(jobName, jobGroupName, trigger.getJobDataMap());
        }
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
    public void updateJob(String jobName, String jobGroupName, int jobTime, int jobTimes, Map<String, ?> jobData,
                          Boolean isStartNow) throws Exception {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (!(trigger instanceof SimpleTrigger)) {
            throw new Exception("非按时间间隔执行的定时任务，不能更改间隔时间，如果要更改定时任务的类型请先删除后再新加！");
        }
        SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;

        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.repeatSecondlyForever(1).withIntervalInSeconds(jobTime);
        if (jobTimes > 0) {
            scheduleBuilder.withRepeatCount(jobTimes);
        }
        TriggerBuilder<SimpleTrigger> triggerBuilder = simpleTrigger.getTriggerBuilder().withIdentity(triggerKey).withDescription(getDescription(jobData)).withSchedule(scheduleBuilder);

        simpleTrigger = triggerBuilder.build();
        JobDetail jobDetail = scheduler.getJobDetail(simpleTrigger.getJobKey());

        simpleTrigger.getJobDataMap().clear();
        jobDetail.getJobDataMap().clear();
        if (jobData != null) {
            simpleTrigger.getJobDataMap().putAll(jobData);
            jobDetail.getJobDataMap().putAll(jobData);
        }
        // 重启触发器
//        scheduler.rescheduleJob(triggerKey, simpleTrigger);
        this.deleteJob(jobName, jobGroupName);
        scheduler.scheduleJob(jobDetail, simpleTrigger);

        // 是否增加时就启动
//        if (isStartNow) {  // simpleTrigger 时，这个参数无效，启动的时候一定会执行一次。
//            this.runAJobNow(jobName, jobGroupName);
//        }
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
     * 暂停一个任务
     *
     * @param jobName
     * @param jobGroupName
     */
    public void pauseTrigger(String jobName, String jobGroupName) throws Exception {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
        scheduler.pauseTrigger(triggerKey);
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
     * 恢复一个Trigger
     *
     * @param jobName
     * @param jobGroupName
     */

    public void resumeTrigger(String jobName, String jobGroupName) throws Exception {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
        scheduler.resumeTrigger(triggerKey);
    }

    public boolean isExists(String jobName, String jobGroupName) throws Exception {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
        return scheduler.checkExists(triggerKey);
    }

    /**
     * 立即执行一个job
     *
     * @param jobName
     * @param jobGroupName
     */

    public void runAJobNow(String jobName, String jobGroupName, Map jobData) throws Exception {
        JobKey jobKey = JobKey.jobKey(jobName, jobGroupName);
        Trigger trigger = scheduler.getTrigger(TriggerKey.triggerKey(jobName, jobGroupName));
        if (jobData != null) {
            trigger.getJobDataMap().putAll(jobData);  // 将直接运行的参数传入进去
        }
        scheduler.triggerJob(jobKey, trigger.getJobDataMap());
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
                Map<String, Object> map = new LinkedHashMap<>();
                putPublicAttr(trigger, jobKey, map);
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
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            putPublicAttr(trigger, jobKey, map);
            map.put(QuartzController.JobFieldName.CLASSNAME.value(), jobDetail.getJobClass());
            map.put(QuartzController.JobFieldName.JOBDATA.value(), jobDetail.getJobDataMap());
            jobList.add(map);
        }
        return jobList;
    }

    private void putPublicAttr(Trigger trigger, JobKey jobKey, Map<String, Object> map) throws Exception {
        map.put(QuartzController.JobFieldName.CLASSNAME.value(), scheduler.getJobDetail(jobKey).getJobClass().getName());
        map.put(QuartzController.JobFieldName.JOBNAME.value(), jobKey.getName());
        map.put(QuartzController.JobFieldName.JOBGROUPNAME.value(), jobKey.getGroup());
        map.put(QuartzController.JobFieldName.DESCRIPTION.value(), "触发器[" + trigger.getKey() + "]");
        Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
        map.put(QuartzController.JobFieldName.JOBSTATUS.value(), triggerState.name());
        if (trigger instanceof CronTrigger) {
            CronTrigger cronTrigger = (CronTrigger) trigger;
            String cronExpression = cronTrigger.getCronExpression();
            map.put(QuartzController.JobFieldName.JOBTIME.value(), cronExpression);
            map.put("timeZone", cronTrigger.getTimeZone());
        } else if (trigger instanceof SimpleTrigger) {
            SimpleTrigger simpleTrigger = (SimpleTrigger) trigger;
            map.put("timesTriggered", simpleTrigger.getTimesTriggered());
            map.put("repeatCount", simpleTrigger.getRepeatCount());
            map.put(QuartzController.JobFieldName.JOBTIME.value(), simpleTrigger.getRepeatInterval() / 1000);
        }
        map.put(QuartzController.JobFieldName.JOBDATA.value(), trigger.getJobDataMap());
        map.put("nextFireTime", trigger.getNextFireTime());
        map.put("previousFireTime", trigger.getPreviousFireTime());
        map.put("finalFireTime", trigger.getFinalFireTime());
        map.put("description", trigger.getDescription());
    }

    private String getDescription(Map datas) {
        if (datas == null) {
            return "";
        }
        Object description = datas.get(QuartzController.JobFieldName.DESCRIPTION.value());
        return description == null ? "" : description.toString();
    }
}
