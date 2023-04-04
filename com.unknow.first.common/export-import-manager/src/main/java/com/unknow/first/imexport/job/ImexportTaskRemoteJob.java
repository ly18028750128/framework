package com.unknow.first.imexport.job;

import com.unknow.first.imexport.constant.ImexportConstants.ProcessStatus;
import com.unknow.first.imexport.domain.FrameExportTemplate;
import com.unknow.first.imexport.domain.FrameImportExportTask;
import com.unknow.first.imexport.feign.ImexportTaskFeignClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.cloud.core.redis.RedisUtil;
import org.cloud.scheduler.constants.MisfireEnum;
import org.cloud.scheduler.job.BaseQuartzJobBean;
import org.cloud.utils.CommonUtil;
import org.cloud.utils.mongo.MongoDBUtil;
import org.cloud.utils.process.ProcessUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static com.unknow.first.imexport.callable.ExportCallableService._TEMP_FILE_PATH;

@Slf4j
public class ImexportTaskRemoteJob extends BaseQuartzJobBean {

    final public static String redisLockerName = "com.unknow.first.imexport.job.ImexportTaskRemoteJob.locker";
    private final RedisUtil redisUtil;
    private final ImexportTaskFeignClient imexportTaskFeignClient;


    public ImexportTaskRemoteJob(ImexportTaskFeignClient imexportTaskFeignClient, RedisUtil redisUtil) {
        this.imexportTaskFeignClient = imexportTaskFeignClient;
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
        List<FrameImportExportTask> noProcessTaskList = imexportTaskFeignClient.listNoProcessTaskByMicroservice(currentMicroservice);

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
                    FrameImportExportTask updateTaskVO = FrameImportExportTask.builder().taskId(importExportTask.getTaskId())
                        .taskStatus(ProcessStatus.processing.value).build();
                    imexportTaskFeignClient.update(updateTaskVO);

                    if (StringUtils.hasLength(importExportTask.getTemplateCode())) {
                        FrameExportTemplate exportTemplate = imexportTaskFeignClient.getExportTemplate(importExportTask.getTemplateCode());
                        if (exportTemplate != null && StringUtils.hasLength(exportTemplate.getFileId())) {
                            InputStream in = MongoDBUtil.single().getInputStreamByObjectId(exportTemplate.getFileId());
                            importExportTask.setTemplateIn(in);
                        } else {
                            throw new RuntimeException(String.format("模板[%s]不存在或者模板文件不存在", importExportTask.getTemplateCode()));
                        }
                    }
                    Constructor constructor = Class.forName(importExportTask.getProcessClass()).getConstructor(FrameImportExportTask.class);
                    Callable<FrameImportExportTask> callable = (Callable<FrameImportExportTask>) constructor.newInstance(importExportTask);
                    callables.add(callable);
                } catch (ClassNotFoundException notFoundException) {
                    log.error("{}", notFoundException);
                    notFoundException.printStackTrace();
                    importExportTask.setTaskStatus(ProcessStatus.fail.value);
                    importExportTask.setMessage(String.format("class [%s] not found", importExportTask.getProcessClass()));
                    imexportTaskFeignClient.update(importExportTask);
                } catch (Exception e) {
                    e.printStackTrace();
                    importExportTask.setTaskStatus(ProcessStatus.fail.value);
                    importExportTask.setMessage("错误描述:" + e.getMessage() + "   -   错误原因: " +e.getCause().getMessage());
                    imexportTaskFeignClient.update(importExportTask);
                }
            }
            if (!callables.isEmpty()) {
                List<FrameImportExportTask> importExportTaskList = ProcessUtil.single().runCallables(callables);
                for (FrameImportExportTask importExportTask : importExportTaskList) {
                    if (importExportTask == null) {
                        continue;
                    }
                    imexportTaskFeignClient.update(importExportTask);
                }
            }
        } finally {
            redisUtil.releaseLock(redisLockerName);
        }
    }
}
