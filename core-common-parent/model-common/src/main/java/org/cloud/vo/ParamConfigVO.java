package org.cloud.vo;

import cn.hutool.core.util.ReflectUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 参数配置
 */
@ApiModel(description = "参数配置")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParamConfigVO implements Serializable {

    @ApiModelProperty(value = "")
    private Integer id;

    /**
     * 配置编码
     */
    @ApiModelProperty(value = "配置编码")
    private String configCode;

    /**
     * 配置名称描述
     */
    @ApiModelProperty(value = "配置名称描述")
    private String configName;

    /**
     * 配置值
     */
    @ApiModelProperty(value = "配置值")
    private String configValue;

    /**
     * 配置类型(10-私有；20公开)
     */
    @ApiModelProperty(value = "配置类型(10-私有；20公开)")
    private Integer configType;

    /**
     * 配置值的只读状态(10-值只读；20-值可修改)
     */
    @ApiModelProperty(value = "配置值的只读状态(10-值只读；20-值可修改)")
    private Integer readonlyStatus;

    /**
     * 拓展字段1
     */
    @ApiModelProperty(value = "拓展字段1")
    private String expand1;

    /**
     * 拓展字段2
     */
    @ApiModelProperty(value = "拓展字段2")
    private String expand2;

    /**
     * 拓展字段3
     */
    @ApiModelProperty(value = "拓展字段3")
    private String expand3;

    public <T> T getValue(Class<T> cls) {
        if (this.configValue == null) {
            return null;
        }
        return ReflectUtil.newInstance(cls, this.getConfigValue());
    }
}