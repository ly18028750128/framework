package com.unknow.first.imexport.domain;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @TableName t_frame_import_export_task
 */
@TableName(value = "t_frame_import_export_task")
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("导出导出任务表")
public class FrameImportExportTask implements Serializable {

    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long taskId;

    /**
     * 任务类型：1(导入) 2(导出)
     */
    @ApiModelProperty(value = "任务类型：1(导入) 2(导出)")
    private Integer taskType;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    /**
     * 导入或者导出的文件名
     */
    @ApiModelProperty("导入或者导出的文件名")
    private String fileName;

    /**
     * 文件id，用于存储导入或者导出的文件objectId
     */
    @ApiModelProperty("文件id，用于存储导入或者导出的文件objectId")
    private String fileId;

    /**
     * 导入或者导出出错时保存错误的文件名
     */
    @ApiModelProperty("导入或者导出出错时保存错误的文件名")
    private String errorFileName;

    /**
     * 文件id，用于存储导入或者导出出错时的文件objectId
     */
    @ApiModelProperty("文件id，用于存储导入或者导出出错时的文件objectId")
    private String errorFileId;

    /**
     * 任务总行数
     */
    @ApiModelProperty("任务总行数")
    private Long dataCount;

    /**
     * 错误行数
     */
    @ApiModelProperty("错误行数")
    private Long dataErrorCount;

    /**
     * 正确行数
     */
    @ApiModelProperty("正确行数")
    private Long dataCorrectCount;

    /**
     * 开始执行时间
     */
    @ApiModelProperty("开始执行时间")
    private Date startTime;

    /**
     * 结束执行时间
     */
    @ApiModelProperty("结束执行时间")
    private Date endTime;

    /**
     * 返回消息
     */
    @ApiModelProperty("返回消息")
    private String message;

    /**
     * 任务状态：1(未执行) 2(执行中)3(执行成功)-1(执行失败)
     */
    @ApiModelProperty("任务状态：1(未执行) 2(执行中)3(执行成功)-1(执行失败)")
    private Integer taskStatus;

    @ApiModelProperty(value = "所属服务", hidden = true)
    private String belongMicroservice;

    /**
     * 创建人ID
     */
    @ApiModelProperty(value = "创建人ID", hidden = true)
    private Long createBy;

    /**
     * 创建人名称
     */
    @ApiModelProperty(value = "创建人名称", hidden = true)
    private String createByName;

    /**
     * 创建日期
     */
    @ApiModelProperty(value = "创建日期", hidden = true)
    private Date createDate;

    /**
     * 更新人ID
     */
    @ApiModelProperty(value = "更新人ID", hidden = true)
    private Long updateBy;

    /**
     * 创建人名称
     */
    @ApiModelProperty(value = "创建人名称", hidden = true)
    private String updateByName;

    /**
     * 更新日期
     */
    @ApiModelProperty(value = "更新日期", hidden = true)
    private Date updateDate;

    /**
     *
     */
    @TableField(updateStrategy = FieldStrategy.NEVER, insertStrategy = FieldStrategy.NEVER)
    private Long executeSeconds;

    /**
     * 线程执行类的名称，导入继承ImportCallableService，导出继承ExportCallableService
     */
    @ApiModelProperty(value = "线程执行类的名称，导入继承ImportCallableService，导出继承ExportCallableService")
    private String processClass;
    @ApiModelProperty("执行参数（JSON）")
    private String params;


    @ApiModelProperty("数据权限，记录当时登录用户的数据权限，方便代码中控制权限（JSON）")
    @JsonIgnore
    private String dataDimension;

    /**
     * 模板编码，导出时有效，对应模板表里的编码
     */
    @ApiModelProperty("模板编码，导出时有效，对应模板表里的编码")
    private String templateCode;

    @TableField(exist = false)
    @JsonIgnore
    private InputStream templateIn;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}