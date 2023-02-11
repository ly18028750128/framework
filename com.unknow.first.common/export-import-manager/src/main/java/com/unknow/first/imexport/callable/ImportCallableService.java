package com.unknow.first.imexport.callable;

import com.unknow.first.imexport.constant.ImexportConstants.ProcessStatus;
import com.unknow.first.imexport.domain.FrameImportExportTask;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.Callable;
import lombok.Getter;
import org.apache.commons.io.IOUtils;
import org.cloud.utils.mongo.MongoDBUtil;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public abstract class ImportCallableService implements Callable<FrameImportExportTask> {

    public final FrameImportExportTask frameImportExportTask;
    @Getter
    private InputStream fileInputStream;

    public ImportCallableService(FrameImportExportTask frameImportExportTask) {
        this.frameImportExportTask = frameImportExportTask;
    }

    /**
     * 初始化相关内容，调用服务生成任务，然后返回对应的执行任务，初始化内容并返回对象，可以没有。
     */
    public abstract void init() throws RuntimeException;

    /**
     * 执行导入导出任务，记录导出及导入成功或者失败的文件。
     *
     * @throws RuntimeException
     */
    public abstract void process() throws RuntimeException;

    /**
     * 执行完后的操作
     *
     * @throws RuntimeException
     */
    public abstract void after() throws RuntimeException;

    @Override
    public FrameImportExportTask call() {
        frameImportExportTask.setStartTime(new Date());
        try {
            Assert.isTrue(StringUtils.hasLength(frameImportExportTask.getFileId()), "system.error.import.file.null");
            this.fileInputStream = MongoDBUtil.single().getInputStreamByObjectId(frameImportExportTask.getFileId());
            this.init();
            this.process();
            this.after();
            frameImportExportTask.setTaskStatus(ProcessStatus.success.value);
        } catch (Exception e) {
            frameImportExportTask.setTaskStatus(ProcessStatus.fail.value);
            frameImportExportTask.setMessage(e.getMessage());
        } finally {
            frameImportExportTask.setEndTime(new Date());
            if (fileInputStream != null) {
                IOUtils.closeQuietly(fileInputStream);
            }
        }
        return frameImportExportTask;
    }
}
