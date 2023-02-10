package com.longyou.comm.service.imexport;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.longyou.comm.dto.imexport.UserExportDTO;
import com.unknow.first.imexport.callable.ImexportCallableService;
import com.unknow.first.imexport.constant.ImexportConstants.ProcessStatus;
import com.unknow.first.imexport.domain.FrameImportExportTask;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.cloud.mybatis.dynamic.DynamicSqlUtil;
import org.cloud.utils.CollectionUtil;
import org.cloud.utils.mongo.MongoDBUtil;
import org.cloud.vo.DynamicSqlQueryParamsVO;
import org.cloud.vo.JavaBeanResultMap;

/**
 * 用户导出类，此类不受spring的管理，请不要用注解导入类
 */
@Slf4j
public class UserExportService extends ImexportCallableService {


    public UserExportService(FrameImportExportTask frameImportExportTask) {
        super(frameImportExportTask);
    }

    @Override
    public void init() throws RuntimeException {
        log.info("用户导出初始化，任务Id：{}", frameImportExportTask.getTaskId());
    }

    @SneakyThrows
    @Override
    public void process(String fileName) throws RuntimeException {
        log.info("用户导出执行中，任务Id：{}", frameImportExportTask.getTaskId());
        List<JavaBeanResultMap<Object>> userList = DynamicSqlUtil.single().listDataBySqlContext("select * from t_frame_user", new DynamicSqlQueryParamsVO());
        List<UserExportDTO> userExportDTOList = CollectionUtil.single().convertListToBean(UserExportDTO.class, userList);
        OutputStream outputStream = Files.newOutputStream(Paths.get(fileName));
        try {
            ExcelWriterBuilder excelWriter = EasyExcel.write(outputStream, UserExportDTO.class);
            excelWriter.sheet("用户信息").doWrite(userExportDTOList);
            this.frameImportExportTask.setTaskStatus(ProcessStatus.success.value);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    @SneakyThrows
    @Override
    public void after() throws RuntimeException {
        log.info("用户导出执行结束，任务Id：{}", frameImportExportTask.getTaskId());
    }
}
