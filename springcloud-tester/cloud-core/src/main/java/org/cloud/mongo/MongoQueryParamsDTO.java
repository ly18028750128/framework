package org.cloud.mongo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * mongo db查询VO
 */
@Data
@ApiModel("mongodb查询条件参数")
public class MongoQueryParamsDTO {
    @ApiModelProperty("查询参数列表")
    private List<MongoQueryParam> params = new ArrayList<>();

    @ApiModelProperty("排序列表")
    private List<MongoQueryOrder> orders = new ArrayList<>();

    @ApiModelProperty("显示字段的列表")
    private Map<String, Boolean> fields = new LinkedHashMap<>();   //显示的字段

}
