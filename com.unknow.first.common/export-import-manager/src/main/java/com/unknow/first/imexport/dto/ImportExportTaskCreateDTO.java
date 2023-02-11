package com.unknow.first.imexport.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("导入导出任务创建DTO")
public class ImportExportTaskCreateDTO {

//    @ApiModelProperty(value = "任务类型：1(导入) 2(导出)")
//    private Integer taskType;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    /**
     * 线程执行类的名称，导出继承ImportCallableService，导出继承ExportCallableService
     */
    @ApiModelProperty(value = "线程执行类的名称，导出继承ImportCallableService，导出继承ExportCallableService")
    private String processClass;

    @ApiModelProperty(value = "所属服务", hidden = true)
    private String belongMicroservice;

    @ApiModelProperty("模板编码，导出时有效，对应模板表里的编码")
    private String templateCode;

    @ApiModelProperty("执行参数（JSON）")
    private String params;

    @ApiModelProperty(value = "文件类型",example = "xlsx")
    private String extension = "xlsx";
}
