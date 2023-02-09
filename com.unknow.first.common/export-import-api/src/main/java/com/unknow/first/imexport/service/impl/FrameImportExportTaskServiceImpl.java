package com.unknow.first.imexport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.unknow.first.imexport.domain.FrameImportExportTask;
import com.unknow.first.imexport.mapper.FrameImportExportTaskMapper;
import com.unknow.first.imexport.service.FrameImportExportTaskService;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 * @description 针对表【t_frame_import_export_task】的数据库操作Service实现
 * @createDate 2023-02-08 14:52:06
 */
@Service
public class FrameImportExportTaskServiceImpl extends ServiceImpl<FrameImportExportTaskMapper, FrameImportExportTask>
    implements FrameImportExportTaskService {

    @Override
    public List<FrameImportExportTask> listNoProcessTaskByMicroservice(String microservice) {
        LambdaQueryWrapper<FrameImportExportTask> query = Wrappers.<FrameImportExportTask>lambdaQuery()
            .eq(FrameImportExportTask::getBelongMicroservice, microservice).orderByAsc(FrameImportExportTask::getTaskId).last("limit 5");

        return this.list(query);
    }
}




