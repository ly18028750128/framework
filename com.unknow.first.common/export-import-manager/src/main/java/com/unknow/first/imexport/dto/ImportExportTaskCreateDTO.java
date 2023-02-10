package com.unknow.first.imexport.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("导入导出任务创建DTO")
public class ImportExportTaskCreateDTO {
    @ApiModelProperty(value = "任务类型：1(导入) 2(导出)")
    private Integer taskType;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    /**
     * 线程执行类的名称，继承ImexportRunnableService
     */
    @ApiModelProperty(value = "线程执行类的名称，继承ImexportRunnableService")
    private String processClass;

    @ApiModelProperty(value = "所属服务", hidden = true)
    private String belongMicroservice;
}
