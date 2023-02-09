package com.longyou.comm.service.imexport;

import com.unknow.first.imexport.callable.ImexportCallableService;
import com.unknow.first.imexport.constant.ImexportConstants.ProcessStatus;
import com.unknow.first.imexport.domain.FrameImportExportTask;
import com.unknow.first.imexport.feign.ImexportTaskFeignClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.cloud.utils.SpringContextUtil;

/**
 * 用户导出类，此类不受spring的管理，请不要用注解导入类
 */
@Slf4j
public class UserExportService extends ImexportCallableService {

    ImexportTaskFeignClient imexportTaskFeignClient;

    public UserExportService(FrameImportExportTask frameImportExportTask) {
        super(frameImportExportTask);
    }

    @Override
    public void init() throws RuntimeException {
        imexportTaskFeignClient = SpringContextUtil.getBean(ImexportTaskFeignClient.class);
        log.info("用户导出初始化，任务Id：{}", frameImportExportTask.getTaskId());
    }

    @Override
    public void process() throws RuntimeException {
        log.info("用户导出执行中，任务Id：{}", frameImportExportTask.getTaskId());
        this.frameImportExportTask.setTaskStatus(ProcessStatus.success.value);
    }

    @SneakyThrows
    @Override
    public void after() throws RuntimeException {
        log.info("用户导出执行结束，任务Id：{}", frameImportExportTask.getTaskId());
    }
}
