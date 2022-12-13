package org.cloud.mongo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Objects;
import lombok.NoArgsConstructor;

/**
 * mongo db查询VO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("mongodb查询参数")
public class MongoQueryParam {

    @ApiModelProperty("查询字段名称")
    private String name;
    @ApiModelProperty("操作类型")
    private MongoEnumVO.MongoOperatorEnum operator;
    @ApiModelProperty("参数类型")
    private MongoEnumVO.DataType dataType;
    @ApiModelProperty("条件连接方式")
    private MongoEnumVO.RelationalOperator relationalOperator;
    private Object value;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MongoQueryParam that = (MongoQueryParam) o;
        return Objects.equals(name, that.name) &&
                operator == that.operator &&
                dataType == that.dataType &&
                relationalOperator == that.relationalOperator &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, operator, dataType, relationalOperator, value);
    }

}
