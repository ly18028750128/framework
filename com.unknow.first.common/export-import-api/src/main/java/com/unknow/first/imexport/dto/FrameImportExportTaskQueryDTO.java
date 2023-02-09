package com.unknow.first.imexport.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.cloud.annotation.Query;
import org.cloud.annotation.Query.Type;

@ApiModel("导入导出任务查询DTO")
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FrameImportExportTaskQueryDTO implements Serializable {


    /**
     * 任务类型：1(导入) 2(导出)
     */
    @ApiModelProperty("任务类型：1(导入) 2(导出)")
    private Integer taskType;

    /**
     * 导入或者导出的文件名
     */
    @ApiModelProperty("导入或者导出的文件名")
    @Query(type = Type.INNER_LIKE)
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
     * 开始执行时间
     */
    @ApiModelProperty("开始执行时间(范围)")
    @Query(type = Type.UNIX_TIMESTAMP)
    private List<Long> startTime;

    /**
     * 结束执行时间
     */
    @ApiModelProperty("结束执行时间(范围)")
    @Query(type = Type.UNIX_TIMESTAMP)
    private List<Long> endTime;

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

    @ApiModelProperty("所属服务")
    private String belongMicroservice;



    /**
     * 创建人名称
     */
    private String createByName;

    /**
     * 创建人名称
     */
    private Long createBy;

    /**
     * 创建日期
     */
    @Query(type = Type.UNIX_TIMESTAMP)
    private List<Long> createDate;



    /**
     * 创建人名称
     */
    private String updateByName;

    /**
     * 更新日期
     */
    @Query(type = Type.UNIX_TIMESTAMP)
    private List<Long> updateDate;



}