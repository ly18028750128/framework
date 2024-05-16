package com.unknow.first.imexport.inner.controller;

import com.unknow.first.imexport.constant.ImexportConstants;
import com.unknow.first.imexport.domain.FrameExportTemplate;
import com.unknow.first.imexport.domain.FrameImportExportTask;
import com.unknow.first.imexport.dto.ImportExportTaskCreateDTO;
import com.unknow.first.imexport.service.FrameExportTemplateService;
import com.unknow.first.imexport.service.FrameImportExportTaskService;
import com.unknow.first.imexport.service.impl.ImexportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping()
@Api(description = "feign：导入导出任务管理", tags = "feign：导入导出任务管理")
public class ImexportInnerController {

    @Autowired
    FrameImportExportTaskService importExportTaskService;

    @Autowired
    FrameExportTemplateService frameExportTemplateService;
    @Autowired
    private ImexportService imexportService;

    @ApiOperation(value = "创建导入导出任务", notes = "创建导入导出任务")
    @PostMapping("/inner/imexport/task")
    public FrameImportExportTask create(ImportExportTaskCreateDTO exportTaskCreateDTO, ImexportConstants.TaskType taskType,
                                        @ApiParam("需要导入的文件，上传时必传") @RequestPart(required = false, name = "file") MultipartFile file) throws Exception {
        return imexportService.create(exportTaskCreateDTO, taskType, file);

    }

    @ApiOperation(value = "更新任务", notes = "更新任务")
    @PutMapping("/inner/imexport/task")
    public FrameImportExportTask update(@RequestBody FrameImportExportTask frameImportExportTask) throws Exception {
        if (importExportTaskService.updateById(frameImportExportTask)) {
            return importExportTaskService.getById(frameImportExportTask.getTaskId());
        }
        return null;
    }

    @ApiOperation(value = "查询当前服务未执行的导出任务", notes = "查询当前服务未执行的导出任务")
    @GetMapping("/inner/imexport/task")
    public List<FrameImportExportTask> listNoProcessTaskByMicroservice(@RequestParam("microservice") String microservice) throws Exception {
        return importExportTaskService.listNoProcessTaskByMicroservice(microservice);

    }

    @ApiOperation(value = "查询当前服务未执行的导出任务", notes = "查询当前服务未执行的导出任务")
    @GetMapping("/inner/export/template")
    public FrameExportTemplate getExportTemplate(@RequestParam("templateCode") String templateCode) throws Exception {
        return frameExportTemplateService.getTemplateByCode(templateCode);

    }

}
