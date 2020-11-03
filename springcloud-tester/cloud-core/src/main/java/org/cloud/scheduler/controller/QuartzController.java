package org.cloud.scheduler.controller;


import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.scheduler.service.QuartzService;
import org.cloud.utils.CollectionUtil;
import org.cloud.utils.SystemStringUtil;
import org.cloud.vo.ResponseResult;
import org.quartz.CronExpression;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/quartz/job/")
@SystemResource(path = "定时任务/quartz")
public class QuartzController {

    @Autowired
    QuartzService quartzService;

    @Autowired
    private Scheduler scheduler;

    public static enum JobFieldName {
        CLASSNAME("jobClass", "类名,一定要继承QuartzJobBean"),
        JOBNAME("jobName", "定时任务名称"),
        JOBGROUPNAME("jobGroupName", "定时任务组名"),
        JOBTIME("jobTime", "定时任务时间，如果输入的是字符表示是corn表达式，如果是数字那么按时间间隔单位：ms"),
        JOBTIMES("jobTimes", "执行次数，小于零表示不限制次数"),
        JOBDATA("jobData", "定时任务参数，类型为map"),
        DESCRIPTION("description", "描述"),
        JOBSTATUS("jobStatus", "描述"),
        JOBTYPE("jobType", "任务类型"),
        ;
        private String name;
        private String desc;

        JobFieldName(String name, String desc) {
            this.name = name;
            this.desc = desc;
        }

        public String value() {
            return this.name;
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public List<Map<String, Object>> queryAllJob() throws Exception {
        return quartzService.queryAllJob();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/allRunJob")
    public List<Map<String, Object>> queryRunJob() throws Exception {
        return quartzService.queryRunJob();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/createOrUpdate")
    @SystemResource(value = "createOrUpdate", description = "创建或更新定时任务", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @Transactional(propagation = Propagation.REQUIRED)
    public ResponseResult createOrUpdate(@RequestBody Map<String, Object> params) throws Exception {
        checkParams(params);
        this.deleteJob(params);
        this.addJob(params);
        return ResponseResult.createSuccessResult();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/delete")
    @SystemResource(value = "deleteJob", description = "删除定时任务", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    public ResponseResult deleteJob(@RequestBody Map<String, Object> params) throws Exception {
        String jobName = (String) params.get(JobFieldName.JOBNAME.value());
        String jobGroupName = (String) params.get(JobFieldName.JOBGROUPNAME.value());
        if (scheduler.checkExists(JobKey.jobKey(jobName, jobGroupName))) {
            quartzService.deleteJob(jobName, jobGroupName);
        }
        return ResponseResult.createSuccessResult();
    }

    // 暂停一个任务定义
    @RequestMapping(method = RequestMethod.POST, value = "/pause")
    @SystemResource(value = "pauseTrigger", description = "暂停定时任务", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    public ResponseResult pauseTrigger(@RequestBody Map<String, Object> params) throws Exception {
        String jobName = (String) params.get(JobFieldName.JOBNAME.value());
        String jobGroupName = (String) params.get(JobFieldName.JOBGROUPNAME.value());
        quartzService.pauseTrigger(jobName, jobGroupName);
        return ResponseResult.createSuccessResult();
    }

    @SystemResource(value = "resumeTrigger", description = "恢复定时任务", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    @RequestMapping(method = RequestMethod.POST, value = "/resume")
    public ResponseResult resumeTrigger(@RequestBody Map<String, Object> params) throws Exception {
        String jobName = (String) params.get(JobFieldName.JOBNAME.value());
        String jobGroupName = (String) params.get(JobFieldName.JOBGROUPNAME.value());
        quartzService.resumeTrigger(jobName, jobGroupName);
        return ResponseResult.createSuccessResult();
    }

    // 暂停一个任务执行的job
    @RequestMapping(method = RequestMethod.POST, value = "/pauseJob")
    @SystemResource(value = "pauseJob", description = "暂停定时任务的JOB", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    public ResponseResult pauseJob(@RequestBody Map<String, Object> params) throws Exception {
        String jobName = (String) params.get(JobFieldName.JOBNAME.value());
        String jobGroupName = (String) params.get(JobFieldName.JOBGROUPNAME.value());
        quartzService.pauseJob(jobName, jobGroupName);
        return ResponseResult.createSuccessResult();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/resumeJob")
    @SystemResource(value = "resumeJob", description = "恢复定时任务的JOB", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    public ResponseResult resumeJob(@RequestBody Map<String, Object> params) throws Exception {
        String jobName = (String) params.get(JobFieldName.JOBNAME.value());
        String jobGroupName = (String) params.get(JobFieldName.JOBGROUPNAME.value());
        quartzService.resumeJob(jobName, jobGroupName);
        return ResponseResult.createSuccessResult();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/runNow")
    @SystemResource(value = "runAJobNow", description = "手动运行定时任务", authMethod = CoreConstant.AuthMethod.BYUSERPERMISSION)
    public ResponseResult runAJobNow(@RequestBody Map<String, Object> params) throws Exception {
        String jobName = (String) params.get(JobFieldName.JOBNAME.value());
        String jobGroupName = (String) params.get(JobFieldName.JOBGROUPNAME.value());
        Map jobData = (Map) params.get(JobFieldName.JOBDATA.value());
        quartzService.runAJobNow(jobName, jobGroupName, jobData);
        return ResponseResult.createSuccessResult();
    }


    private void addJob(Map<String, Object> params) throws Exception {

        Class<? extends QuartzJobBean> jobClass = null;
        String clsName = params.get(JobFieldName.CLASSNAME.value()).toString();
        try {
            jobClass = (Class<? extends QuartzJobBean>) Class.forName(clsName);
        } catch (Exception e) {
            throw new Exception(clsName + " 不存在，或者不是继承QuartzJobBean");
        }

        String jobName = (String) params.get(JobFieldName.JOBNAME.value());
        String jobGroupName = (String) params.get(JobFieldName.JOBGROUPNAME.value());
        Object jobTime = params.get(JobFieldName.JOBTIME.value());
        Map jobData = (Map) params.get(JobFieldName.JOBDATA.value());
        Boolean isStarNow = (Boolean) params.get("isStartNow");
        isStarNow = (isStarNow == null ? false : isStarNow);
        if (jobTime instanceof String) { // 按corn表达式
            quartzService.addJob(jobClass, jobName, jobGroupName, (String) jobTime, jobData, isStarNow);
        } else {  // 按时间间隔
            int jobTimes = -1;
            if (params.get(JobFieldName.JOBTIMES.value()) != null) {
                jobTimes = Integer.parseInt(params.get(JobFieldName.JOBTIMES.value()).toString());
            }
            TimeUnit timeUnit = null;
            if (CollectionUtil.single().isEmpty(params.get("timeUnit"))) {
                timeUnit = TimeUnit.SECONDS;
            } else {
                timeUnit = TimeUnit.valueOf(params.get("timeUnit").toString());
            }
            quartzService.addJob(jobClass, jobName, jobGroupName, timeUnit, Integer.parseInt(jobTime.toString()), jobTimes, jobData);
        }
    }


    private void checkParams(Map<String, Object> params) throws Exception {
        Object jobTime = params.get(JobFieldName.JOBTIME.value());

        Boolean result = true;

        if ((jobTime instanceof String) && !CronExpression.isValidExpression((String) jobTime)) {
            throw new Exception("非法的cron表达式");
        }
        String jobName = (String) params.get(JobFieldName.JOBNAME.value());

        if (SystemStringUtil.single().isEmpty(jobName)) {
            throw new Exception("jobName不能为空");
        }

        String jobGroupName = (String) params.get(JobFieldName.JOBGROUPNAME.value());
        if (SystemStringUtil.single().isEmpty(jobGroupName)) {
            throw new Exception("jobGroupName不能为空");
        }
    }
}
