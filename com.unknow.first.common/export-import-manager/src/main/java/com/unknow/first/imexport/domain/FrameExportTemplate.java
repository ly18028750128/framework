package com.unknow.first.imexport.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 导出模板表
 * @TableName t_frame_export_template
 */
@TableName(value ="t_frame_export_template")
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("导出模板")
public class FrameExportTemplate implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer templateId;

    /**
     * 模板编码
     */
    @ApiModelProperty("模板编码")
    private String templateCode;

    /**
     * 模板类型：10(excel),20(pdf),30(word)
     */
    @ApiModelProperty("模板类型：10(excel),20(pdf),30(word)")
    private Integer templateType;

    /**
     * 文件ID，存储在mongodb里的文件ObjectId
     */
    @ApiModelProperty("文件ID，存储在mongodb里的文件ObjectId")
    private String fileId;

    /**
     * 状态，1/有效 0/无效
     */
    @ApiModelProperty("状态，1/有效 0/无效")
    private Integer status;

    /**
     * 创建人ID
     */
    @ApiModelProperty("创建人ID")
    private Long createBy;

    /**
     * 创建人名称
     */
    @ApiModelProperty("创建人名称")
    private String createByName;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 更新人ID
     */
    private Long updateBy;

    /**
     * 创建人名称
     */
    private String updateByName;

    /**
     * 更新日期
     */
    private Date updateDate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}