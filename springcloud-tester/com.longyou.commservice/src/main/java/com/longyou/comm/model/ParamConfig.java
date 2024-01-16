package com.longyou.comm.model;

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
import lombok.NoArgsConstructor;

/**
    * 参数配置
    */
@ApiModel(description="参数配置")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_param_config")
public class ParamConfig implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="")
    private Integer id;

    /**
     * 配置编码
     */
    @TableField(value = "config_code")
    @ApiModelProperty(value="配置编码")
    private String configCode;

    /**
     * 配置名称描述
     */
    @TableField(value = "config_name")
    @ApiModelProperty(value="配置名称描述")
    private String configName;

    /**
     * 配置值
     */
    @TableField(value = "config_value")
    @ApiModelProperty(value="配置值")
    private String configValue;

    /**
     * 配置类型(10-私有；20公开)
     */
    @TableField(value = "config_type")
    @ApiModelProperty(value="配置类型(10-私有；20公开)")
    private Integer configType;

    /**
     * 配置值的只读状态(10-值只读；20-值可修改)
     */
    @TableField(value = "readonly_status")
    @ApiModelProperty(value="配置值的只读状态(10-值只读；20-值可修改)")
    private Integer readonlyStatus;

    /**
     * 拓展字段1
     */
    @TableField(value = "expand_1")
    @ApiModelProperty(value="拓展字段1")
    private String expand1;

    /**
     * 拓展字段2
     */
    @TableField(value = "expand_2")
    @ApiModelProperty(value="拓展字段2")
    private String expand2;

    /**
     * 拓展字段3
     */
    @TableField(value = "expand_3")
    @ApiModelProperty(value="拓展字段3")
    private String expand3;

    @TableField(value = "create_time")
    @ApiModelProperty(value="")
    private Date createTime;

    @TableField(value = "update_time")
    @ApiModelProperty(value="")
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField(value = "create_by")
    @ApiModelProperty(value="创建人")
    private String createBy;

    /**
     * 更新人
     */
    @TableField(value = "update_by")
    @ApiModelProperty(value="更新人")
    private String updateBy;

    public static final String COL_ID = "id";

    public static final String COL_CONFIG_CODE = "config_code";

    public static final String COL_CONFIG_NAME = "config_name";

    public static final String COL_CONFIG_VALUE = "config_value";

    public static final String COL_CONFIG_TYPE = "config_type";

    public static final String COL_READONLY_STATUS = "readonly_status";

    public static final String COL_EXPAND_1 = "expand_1";

    public static final String COL_EXPAND_2 = "expand_2";

    public static final String COL_EXPAND_3 = "expand_3";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_UPDATE_TIME = "update_time";

    public static final String COL_CREATE_BY = "create_by";

    public static final String COL_UPDATE_BY = "update_by";

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取配置编码
     *
     * @return config_code - 配置编码
     */
    public String getConfigCode() {
        return configCode;
    }

    /**
     * 设置配置编码
     *
     * @param configCode 配置编码
     */
    public void setConfigCode(String configCode) {
        this.configCode = configCode;
    }

    /**
     * 获取配置名称描述
     *
     * @return config_name - 配置名称描述
     */
    public String getConfigName() {
        return configName;
    }

    /**
     * 设置配置名称描述
     *
     * @param configName 配置名称描述
     */
    public void setConfigName(String configName) {
        this.configName = configName;
    }

    /**
     * 获取配置值
     *
     * @return config_value - 配置值
     */
    public String getConfigValue() {
        return configValue;
    }

    /**
     * 设置配置值
     *
     * @param configValue 配置值
     */
    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    /**
     * 获取配置类型(10-私有；20公开)
     *
     * @return config_type - 配置类型(10-私有；20公开)
     */
    public Integer getConfigType() {
        return configType;
    }

    /**
     * 设置配置类型(10-私有；20公开)
     *
     * @param configType 配置类型(10-私有；20公开)
     */
    public void setConfigType(Integer configType) {
        this.configType = configType;
    }

    /**
     * 获取配置值的只读状态(10-值只读；20-值可修改)
     *
     * @return readonly_status - 配置值的只读状态(10-值只读；20-值可修改)
     */
    public Integer getReadonlyStatus() {
        return readonlyStatus;
    }

    /**
     * 设置配置值的只读状态(10-值只读；20-值可修改)
     *
     * @param readonlyStatus 配置值的只读状态(10-值只读；20-值可修改)
     */
    public void setReadonlyStatus(Integer readonlyStatus) {
        this.readonlyStatus = readonlyStatus;
    }

    /**
     * 获取拓展字段1
     *
     * @return expand_1 - 拓展字段1
     */
    public String getExpand1() {
        return expand1;
    }

    /**
     * 设置拓展字段1
     *
     * @param expand1 拓展字段1
     */
    public void setExpand1(String expand1) {
        this.expand1 = expand1;
    }

    /**
     * 获取拓展字段2
     *
     * @return expand_2 - 拓展字段2
     */
    public String getExpand2() {
        return expand2;
    }

    /**
     * 设置拓展字段2
     *
     * @param expand2 拓展字段2
     */
    public void setExpand2(String expand2) {
        this.expand2 = expand2;
    }

    /**
     * 获取拓展字段3
     *
     * @return expand_3 - 拓展字段3
     */
    public String getExpand3() {
        return expand3;
    }

    /**
     * 设置拓展字段3
     *
     * @param expand3 拓展字段3
     */
    public void setExpand3(String expand3) {
        this.expand3 = expand3;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取创建人
     *
     * @return create_by - 创建人
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置创建人
     *
     * @param createBy 创建人
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取更新人
     *
     * @return update_by - 更新人
     */
    public String getUpdateBy() {
        return updateBy;
    }

    /**
     * 设置更新人
     *
     * @param updateBy 更新人
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}