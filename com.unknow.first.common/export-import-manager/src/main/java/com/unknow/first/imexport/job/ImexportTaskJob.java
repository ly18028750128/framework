package com.unknow.first.imexport.job;

import static com.unknow.first.imexport.callable.ExportCallableService._TEMP_FILE_PATH;

import com.unknow.first.imexport.constant.ImexportConstants.ProcessStatus;
import com.unknow.first.imexport.domain.FrameExportTemplate;
import com.unknow.first.imexport.domain.FrameImportExportTask;
import com.unknow.first.imexport.service.FrameExportTemplateService;
import com.unknow.first.imexport.service.FrameImportExportTaskService;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import lombok.SneakyThrows;
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

public class ImexportTaskJob extends BaseQuartzJobBean {

    final public static String redisLockerName = "com.unknow.first.imexport.job.ImexportTaskRemoteJob.locker";
    private final RedisUtil redisUtil;
    private final FrameImportExportTaskService importExportTaskService;

    private final FrameExportTemplateService exportTemplateService;
    public ImexportTaskJob(FrameImportExportTaskService importExportTaskService, FrameExportTemplateService exportTemplateService, RedisUtil redisUtil) {
        this.importExportTaskService = importExportTaskService;
        this.exportTemplateService = exportTemplateService;
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
                    FrameImportExportTask updateTaskVO = FrameImportExportTask.builder().taskId(importExportTask.getTaskId())
                        .taskStatus(ProcessStatus.processing.value).build();
                    importExportTaskService.updateById(updateTaskVO);

                    if (StringUtils.hasLength(importExportTask.getTemplateCode())) {
                        FrameExportTemplate exportTemplate = exportTemplateService.getTemplateByCode(importExportTask.getTemplateCode());
                        if (exportTemplate != null && StringUtils.hasLength(exportTemplate.getFileId())) {
                            InputStream in = MongoDBUtil.single().getInputStreamByObjectId(exportTemplate.getFileId());
                            importExportTask.setTemplateIn(in);
                        } else {
                            throw new JobExecutionException(String.format("模板[%s]不存在或者模板文件不存在", exportTemplate.getTemplateCode()));
                        }
                    }
                    Constructor constructor = Class.forName(importExportTask.getProcessClass()).getConstructor(FrameImportExportTask.class);
                    callables.add((Callable<FrameImportExportTask>) constructor.newInstance(importExportTask));
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
