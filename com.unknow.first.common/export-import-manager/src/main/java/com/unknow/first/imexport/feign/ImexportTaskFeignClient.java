package com.unknow.first.imexport.feign;

import com.unknow.first.imexport.constant.ImexportConstants.TaskType;
import com.unknow.first.imexport.domain.FrameExportTemplate;
import com.unknow.first.imexport.domain.FrameImportExportTask;
import io.swagger.annotations.ApiParam;
import java.io.Serializable;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "${spring.application.group:}COMMON-SERVICE", contextId = "onImexportTaskFeignClient")  // 不区分大小写
public interface ImexportTaskFeignClient extends Serializable {

    @PutMapping(value = "/inner/imexport/task", consumes = MediaType.APPLICATION_JSON_VALUE)
    FrameImportExportTask update(@RequestBody FrameImportExportTask frameImportExportTask) throws Exception;

    @GetMapping(value = "/inner/imexport/task")
    List<FrameImportExportTask> listNoProcessTaskByMicroservice(@RequestParam("microservice") String microservice) throws Exception;

    @GetMapping("/inner/export/template")
    FrameExportTemplate getExportTemplate(@RequestParam("templateCode") String templateCode) throws Exception;

    @PostMapping(value = "/user/imexport/task", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    FrameImportExportTask create(
        @ApiParam("任务名称") @RequestParam(value = "taskName") String taskName,
        @ApiParam("线程执行类的名称，导入继承ImportCallableService，导出继承ExportCallableService") @RequestParam(value = "processClass") String processClass,
        @ApiParam("模板编码，导出时有效，对应模板表里的编码") @RequestParam(value = "templateCode", required = false) String templateCode,
        @ApiParam("执行参数（JSON）") @RequestParam(value = "params", required = false) String params,
        @ApiParam(value = "文件类型", example = "xlsx") @RequestParam(value = "extension", defaultValue = "xlsx") String extension,
        @ApiParam(value = "所属服务") @RequestParam(value = "belongMicroservice") String belongMicroservice,
        @ApiParam(value = "任务类型") @RequestParam(value = "taskType") TaskType taskType,
        @ApiParam("需要导入的文件，上传时必传") @RequestPart(required = false, name = "file") MultipartFile file) throws Exception;

}