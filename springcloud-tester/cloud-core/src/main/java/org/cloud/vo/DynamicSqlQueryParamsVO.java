package org.cloud.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
@ApiModel(value = "DynamicSqlQueryParamsVO", description = "动态SQL参数")
public class DynamicSqlQueryParamsVO {

    @ApiModelProperty(value = "参数值(MAP)")
    private Map<String, Object> params = new LinkedHashMap<>();   // 参数
    @ApiModelProperty(value = "排序字段")
    private String sorts;  // 排序字段

}
