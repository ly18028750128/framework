package com.unknow.first.imexport.job;

import static com.unknow.first.imexport.callable.ImexportCallableService._TEMP_FILE_PATH;

import com.unknow.first.imexport.callable.ImexportCallableService;
import com.unknow.first.imexport.constant.ImexportConstants.ProcessStatus;
import com.unknow.first.imexport.domain.FrameImportExportTask;
import com.unknow.first.imexport.service.FrameImportExportTaskService;
import java.io.File;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import lombok.SneakyThrows;
import org.cloud.core.redis.RedisUtil;
import org.cloud.scheduler.constants.MisfireEnum;
import org.cloud.scheduler.job.BaseQuartzJobBean;
import org.cloud.utils.CommonUtil;
import org.cloud.utils.process.ProcessUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.util.CollectionUtils;

public class ImexportTaskJob extends BaseQuartzJobBean {

    final public static String redisLockerName = "com.unknow.first.imexport.job.ImexportTaskRemoteJob.locker";
    private final RedisUtil redisUtil;
    private final FrameImportExportTaskService importExportTaskService;


    public ImexportTaskJob(FrameImportExportTaskService importExportTaskService, RedisUtil redisUtil) {
        this.importExportTaskService = importExportTaskService;
        this.redisUtil = redisUtil;
    }

    @Override
    protected void init() {
        this.jobName = "导入导出任务";
        jobData.put("description", "导入导出任务，10秒执行一次");
        this.jobTime = "0/10 * * * * ? ";
        if (!new File(_TEMP_FILE_PATH).exists()) {
            new File(_TEMP_FILE_PATH).mkdirs();
        }
        setMisfire(MisfireEnum.CronScheduleMisfireEnum.MISFIRE_INSTRUCTION_DO_NOTHING);
    }

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String currentMicroservice = CommonUtil.single().getEnv("spring.application.name", "");
        List<FrameImportExportTask> noProcessTaskList = importExportTaskService.listNoProcessTaskByMicroservice(currentMicroservice);

        if (CollectionUtils.isEmpty(noProcessTaskList)) {
            return;
        }

        boolean locked = redisUtil.getLock(redisLockerName, 3600 * 1000L);
        if (!locked) {
            return;
        }
        try {
            List<Callable<FrameImportExportTask>> callables = new ArrayList<>();
            for (FrameImportExportTask importExportTask : noProcessTaskList) {
                try {
                    Constructor constructor = Class.forName(importExportTask.getProcessClass()).getConstructor(FrameImportExportTask.class);
                    ImexportCallableService imexportCallableService = (ImexportCallableService) constructor.newInstance(importExportTask);
                    callables.add(imexportCallableService);
                } catch (Exception e) {
                    importExportTask.setTaskStatus(ProcessStatus.fail.value);
                    importExportTask.setMessage(e.getMessage());
                    importExportTaskService.updateById(importExportTask);
                }

            }
            if (!callables.isEmpty()) {
                List<FrameImportExportTask> importExportTaskList = ProcessUtil.single().runCallables(callables);
                for (FrameImportExportTask importExportTask : importExportTaskList) {
                    if (importExportTask == null) {
                        continue;
                    }
                    importExportTaskService.updateById(importExportTask);
                }
            }
        } finally {
            redisUtil.releaseLock(redisLockerName);
        }
    }
}
