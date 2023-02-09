package com.unknow.first.imexport.feign;

import com.unknow.first.imexport.domain.FrameImportExportTask;
import java.io.Serializable;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${spring.application.group:}COMMON-SERVICE", contextId = "onImexportTaskFeignClient")  // 不区分大小写
public interface ImexportTaskFeignClient extends Serializable {

    @PutMapping(value = "/inner/imexport/task", consumes = MediaType.APPLICATION_JSON_VALUE)
    FrameImportExportTask update(@RequestBody FrameImportExportTask frameImportExportTask) throws Exception;

    @GetMapping(value = "/inner/imexport/task")
    List<FrameImportExportTask> listNoProcessTaskByMicroservice(@RequestParam("microservice") String microservice) throws Exception;

}