package com.unknow.first.mongo.vo;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.Min;
import lombok.Data;

@Data
public class MongoPagedParam implements Serializable {

    @ApiModelProperty(value = "显示字段", example = "")
    private String columns;
    @ApiModelProperty(value = "页数", required = true, example = "1")
    @Min(value = 1, message = "不能小于1")
    private Integer page = 1;
    @ApiModelProperty(value = "条数", required = true, example = "10")
    private Integer limit;

    {
        limit = 10;
    }

    /**
     * 获取起始行数
     *
     * @return
     */
    public Integer getStart() {
        return (page - 1) * limit;
    }

    @ApiModelProperty(value = "排序参数", example = "_id asc")
    private String sorts;

}