package org.cloud.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * tom 2022-09-30
 * 用于解析数据权限的配置 例如: <dataDimension>[{"fieldName":"t_system_dic_master.belong_micro_service","dimensionName":"micro_service_name","operator":"=",
 * "connector":"or"},{"fieldName":"t_system_dic_master.belong_micro_service","dimensionName":"micro_service_name","operator":"=",
 * "connector":"or"}]</dataDimension>
 */
@Data
@NoArgsConstructor
@ApiModel(value = "数据权限VO")
public class DataDimensionCondition {

    @ApiModelProperty("字段名称")
    private String fieldName;   //字段名称
    @ApiModelProperty("数据维度名称")
    private String dimensionName; //数据维度名称
    @ApiModelProperty("操作符 如like，默认为=")
    private String operator = "="; //操作符 如like，默认为=
    @ApiModelProperty("连接符，默认为and")
    private String connector = "and"; // 连接符，默认为and
    @ApiModelProperty("数据类型，默认为VARCHAR")
    private String jdbcType = "VARCHAR"; // 数据类型，默认为VARCHAR
}
