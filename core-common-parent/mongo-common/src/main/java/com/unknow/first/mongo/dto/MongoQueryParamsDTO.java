package com.unknow.first.mongo.dto;

import com.unknow.first.mongo.vo.MongoQueryOrder;
import com.unknow.first.mongo.param.MongoQueryParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;

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
