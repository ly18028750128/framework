package com.unknow.first.imexport.service;

import com.unknow.first.imexport.domain.FrameImportExportTask;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.springframework.web.bind.annotation.RequestParam;

/**
* @author Administrator
* @description 针对表【t_frame_import_export_task】的数据库操作Service
* @createDate 2023-02-08 14:52:06
*/
public interface FrameImportExportTaskService extends IService<FrameImportExportTask> {
    List<FrameImportExportTask> listNoProcessTaskByMicroservice( String microservice);
}
