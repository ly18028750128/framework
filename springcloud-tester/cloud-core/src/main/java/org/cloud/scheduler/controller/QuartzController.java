package org.cloud.scheduler.controller;


import org.cloud.scheduler.service.QuartzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/quartz/job/")
public class QuartzController {

    @Autowired
    QuartzService quartzService;

    public static enum JobFieldName {
        CLASSNAME("jobClass", "类名,一定要继承QuartzJobBean"),
        JOBNAME("jobName", "定时任务名称"),
        JOBGROUPNAME("jobGroupName", "定时任务组名"),
        JOBTIME("jobTime", "定时任务时间，如果输入的是字符表示是corn表达式，如果是数字那么按时间间隔单位：ms"),
        JOBTIMES("jobTimes", "执行次数，小于零表示不限制次数"),
        JOBDATA("jobData", "定时任务参数，类型为map"),
        DESCRIPTION("description", "描述"),
        JOBSTATUS("jobStatus", "描述"),
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

    @RequestMapping(method = RequestMethod.POST, value = "/add")
    public void addJob(@RequestBody Map<String, Object> params) throws Exception {

        Class<? extends QuartzJobBean> jobClass = null;
        String clsName = params.get(JobFieldName.CLASSNAME.value()).toString();
        try{
            jobClass = (Class<? extends QuartzJobBean>) Class.forName(clsName);
        }catch (Exception e){
            throw new Exception(clsName+" 不存在，或者不是继承QuartzJobBean");
        }


        String jobName = (String)params.get(JobFieldName.JOBNAME.value());
        String jobGroupName = (String)params.get(JobFieldName.JOBGROUPNAME.value());
        Object jobTime = params.get(JobFieldName.JOBTIME.value());
        Map jobData = (Map) params.get(JobFieldName.JOBDATA.value());
        Boolean isStarNow = (Boolean) params.get("isStartNow");
        isStarNow = (isStarNow==null?false:isStarNow);
        if (jobTime instanceof String) { // 按corn表达式
            quartzService.addJob(jobClass, jobName, jobGroupName, (String) jobTime, jobData,isStarNow);
        } else {  // 按时间间隔
            int jobTimes = -1;
            if (params.get(JobFieldName.JOBTIMES.value()) != null) {
                jobTimes = Integer.parseInt(params.get(JobFieldName.JOBTIMES.value()).toString());
            }
            quartzService.addJob(jobClass, jobName, jobGroupName, Integer.parseInt(jobTime.toString()), jobTimes, jobData,isStarNow);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/update")
    public void updateJob(@RequestBody Map<String, Object> params) throws Exception {
        String jobName = (String) params.get(JobFieldName.JOBNAME.value());
        String jobGroupName = (String) params.get(JobFieldName.JOBGROUPNAME.value());
        Object jobTime = params.get(JobFieldName.JOBTIME.value());
        Map<String,?> jobData = (Map) params.get(JobFieldName.JOBDATA.value());
        Boolean isStarNow = (Boolean) params.get("isStartNow");
        isStarNow = (isStarNow==null?false:isStarNow);
        if (jobTime instanceof String) { // 按corn表达式
            quartzService.updateJob(jobName, jobGroupName, (String) jobTime,jobData,isStarNow);
        } else {  // 按时间间隔
            int jobTimes = -1;
            if (params.get(JobFieldName.JOBTIMES.value()) != null) {
                jobTimes = Integer.parseInt(params.get(JobFieldName.JOBTIMES.value()).toString());
            }
            quartzService.updateJob(jobName, jobGroupName, Integer.parseInt(jobTime.toString()), jobTimes,jobData,isStarNow);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/delete")
    public void deleteJob(@RequestBody Map<String, Object> params) throws Exception {
        String jobName = (String) params.get(JobFieldName.JOBNAME.value());
        String jobGroupName = (String) params.get(JobFieldName.JOBGROUPNAME.value());
        quartzService.deleteJob(jobName, jobGroupName);
    }
    // 暂停一个任务定义
    @RequestMapping(method = RequestMethod.POST, value = "/pause")
    public void pauseTrigger(@RequestBody Map<String, Object> params) throws Exception {
        String jobName = (String) params.get(JobFieldName.JOBNAME.value());
        String jobGroupName = (String) params.get(JobFieldName.JOBGROUPNAME.value());
        quartzService.pauseTrigger(jobName, jobGroupName);
    }

    // 暂停一个任务执行的job
    @RequestMapping(method = RequestMethod.POST, value = "/pauseJob")
    public void pauseJob(@RequestBody Map<String, Object> params) throws Exception {
        String jobName = (String) params.get(JobFieldName.JOBNAME.value());
        String jobGroupName = (String) params.get(JobFieldName.JOBGROUPNAME.value());
        quartzService.pauseJob(jobName, jobGroupName);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/resumeJob")
    public void resumeJob(@RequestBody Map<String, Object> params) throws Exception {
        String jobName = (String) params.get(JobFieldName.JOBNAME.value());
        String jobGroupName = (String) params.get(JobFieldName.JOBGROUPNAME.value());
        quartzService.resumeJob(jobName, jobGroupName);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/resume")
    public void resumeTrigger(@RequestBody Map<String, Object> params) throws Exception {
        String jobName = (String) params.get(JobFieldName.JOBNAME.value());
        String jobGroupName = (String) params.get(JobFieldName.JOBGROUPNAME.value());
        quartzService.resumeTrigger(jobName, jobGroupName);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/runNow")
    public void runAJobNow(@RequestBody Map<String, Object> params) throws Exception {
        String jobName = (String) params.get(JobFieldName.JOBNAME.value());
        String jobGroupName = (String) params.get(JobFieldName.JOBGROUPNAME.value());
        quartzService.runAJobNow(jobName, jobGroupName);
    }

}
