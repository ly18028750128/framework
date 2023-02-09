package com.unknow.first.imexport.inner.controller;

import com.unknow.first.imexport.domain.FrameImportExportTask;
import com.unknow.first.imexport.service.FrameImportExportTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inner/imexport/task")
@Api(description = "feign：导入导出任务管理", tags = "feign：导入导出任务管理")
public class ImexportInnerController {

    @Autowired
    FrameImportExportTaskService importExportTaskService;

    @ApiOperation(value = "更新任务", notes = "更新任务")
    @PutMapping()
    public FrameImportExportTask update(@RequestBody FrameImportExportTask frameImportExportTask) throws Exception {
        if (importExportTaskService.updateById(frameImportExportTask)) {
            return importExportTaskService.getById(frameImportExportTask.getTaskId());
        }
        return null;
    }

    @ApiOperation(value = "查询当前服务未执行的导出任务", notes = "查询当前服务未执行的导出任务")
    @GetMapping()
    public List<FrameImportExportTask> listNoProcessTaskByMicroservice(@RequestParam("microservice") String microservice) throws Exception {
        return importExportTaskService.listNoProcessTaskByMicroservice(microservice);

    }

}
