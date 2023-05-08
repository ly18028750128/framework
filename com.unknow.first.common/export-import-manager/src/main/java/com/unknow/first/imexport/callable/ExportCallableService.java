package com.unknow.first.imexport.callable;

import com.unknow.first.imexport.constant.ImexportConstants.ProcessStatus;
import com.unknow.first.imexport.domain.FrameImportExportTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.cloud.constant.MimeTypeEnum;
import org.cloud.utils.mongo.MongoDBUtil;

import java.io.File;
import java.util.Date;
import java.util.concurrent.Callable;

@Slf4j
public abstract class ExportCallableService implements Callable<FrameImportExportTask> {

    public final static String _TEMP_FILE_PATH = "/temp/imexport/";

    public final FrameImportExportTask frameImportExportTask;

    public ExportCallableService(FrameImportExportTask frameImportExportTask) {
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
    public abstract void process(String fileName) throws RuntimeException;

    /**
     * 执行完后的操作
     *
     * @throws RuntimeException
     */
    public abstract void after() throws RuntimeException;

    @Override
    public FrameImportExportTask call() {
        frameImportExportTask.setStartTime(new Date());
        final String fileName = _TEMP_FILE_PATH + frameImportExportTask.getFileName();
        try {
            this.init();
            this.process(fileName);
            ObjectId fileId = MongoDBUtil.single().storePersonFile(fileName, MimeTypeEnum.contentTypeByFileName(fileName), frameImportExportTask.getCreateBy(),
                frameImportExportTask.getCreateByName());
            this.after();
            frameImportExportTask.setFileId(fileId.toString());
            frameImportExportTask.setTaskStatus(ProcessStatus.success.value);
        } catch (Exception e) {
            e.printStackTrace();
            frameImportExportTask.setTaskStatus(ProcessStatus.fail.value);
            frameImportExportTask.setMessage("错误描述:" + e.getMessage() + "   -   错误原因: " + (e.getCause() == null ? "空" : e.getCause().getMessage()));
        } finally {
            frameImportExportTask.setEndTime(new Date());
            new File(fileName).delete();
            if(frameImportExportTask.getTemplateIn()!=null){
                IOUtils.closeQuietly(frameImportExportTask.getTemplateIn());
            }
        }
        return frameImportExportTask;
    }
}
