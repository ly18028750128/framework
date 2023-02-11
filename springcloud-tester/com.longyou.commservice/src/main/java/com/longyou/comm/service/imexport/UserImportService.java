package com.longyou.comm.service.imexport;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import com.longyou.comm.dto.imexport.UserExportDTO;
import com.unknow.first.imexport.callable.ImportCallableService;
import com.unknow.first.imexport.domain.FrameImportExportTask;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserImportService extends ImportCallableService {

    public UserImportService(FrameImportExportTask frameImportExportTask) {
        super(frameImportExportTask);
    }

    @Override
    public void init() throws RuntimeException {
        log.info("用户导入初始化，任务Id：{}", frameImportExportTask.getTaskId());
    }

    @Override
    public void process() throws RuntimeException {
        log.info("用户导入执行中，任务Id：{}", frameImportExportTask.getTaskId());
        EasyExcel.read(this.getFileInputStream(), UserExportDTO.class, new ReadListener<UserExportDTO>() {

            /**
             * 单次缓存的数据量
             */
            public static final int BATCH_COUNT = 100;
            /**
             *临时存储
             */
            private List<UserExportDTO> userExportDTOList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

            @Override
            public void invoke(UserExportDTO data, AnalysisContext context) {
                userExportDTOList.add(data);
                if (userExportDTOList.size() >= BATCH_COUNT) {
                    log.info("保存数据：{}", JSON.toJSONString(userExportDTOList));
                    // 存储完成清理 list
                    userExportDTOList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
               if(!userExportDTOList.isEmpty()){
                   log.info("保存尾部数据：{}", JSON.toJSONString(userExportDTOList));
               }
            }
        }).sheet().doRead();
    }

    @Override
    public void after() throws RuntimeException {
        log.info("用户导入执行后处理，任务Id：{}", frameImportExportTask.getTaskId());
    }
}
