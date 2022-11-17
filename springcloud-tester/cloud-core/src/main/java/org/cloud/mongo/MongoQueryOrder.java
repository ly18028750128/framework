package org.cloud.mongo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * mongo db排序VO
 */
@Data
@ApiModel("mongo查询排序项")
public class MongoQueryOrder {

    @ApiModelProperty("排序方式")
    private String direction;
    @ApiModelProperty("排序属性名")
    private String property;

}
