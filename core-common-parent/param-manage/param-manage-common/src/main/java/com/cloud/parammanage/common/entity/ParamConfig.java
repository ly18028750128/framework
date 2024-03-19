package com.cloud.parammanage.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 参数配置
 */
@ApiModel(description = "参数配置")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_param_config")
public class ParamConfig {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "")
    private Integer id;

    /**
     * 配置编码
     */
    @TableField(value = "config_code")
    @ApiModelProperty(value = "配置编码")
    private String configCode;

    /**
     * 配置名称描述
     */
    @TableField(value = "config_name")
    @ApiModelProperty(value = "配置名称描述")
    private String configName;

    /**
     * 配置值
     */
    @TableField(value = "config_value")
    @ApiModelProperty(value = "配置值")
    private String configValue;

    /**
     * 配置类型(10-私有；20公开)
     */
    @TableField(value = "config_type")
    @ApiModelProperty(value = "配置类型(10-私有；20公开)")
    private Integer configType;

    /**
     * 配置值的只读状态(10-值只读；20-值可修改)
     */
    @TableField(value = "readonly_status")
    @ApiModelProperty(value = "配置值的只读状态(10-值只读；20-值可修改)")
    private Integer readonlyStatus;

    /**
     * 拓展字段1
     */
    @TableField(value = "expand_1")
    @ApiModelProperty(value = "拓展字段1")
    private String expand1;

    /**
     * 拓展字段2
     */
    @TableField(value = "expand_2")
    @ApiModelProperty(value = "拓展字段2")
    private String expand2;

    /**
     * 拓展字段3
     */
    @TableField(value = "expand_3")
    @ApiModelProperty(value = "拓展字段3")
    private String expand3;

    @TableField(value = "create_time")
    @ApiModelProperty(value = "")
    private Date createTime;

    @TableField(value = "update_time")
    @ApiModelProperty(value = "")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField(value = "create_by")
    @ApiModelProperty(value = "创建人")
    private String createBy;

    /**
     * 更新人
     */
    @TableField(value = "update_by")
    @ApiModelProperty(value = "更新人")
    private String updateBy;
}