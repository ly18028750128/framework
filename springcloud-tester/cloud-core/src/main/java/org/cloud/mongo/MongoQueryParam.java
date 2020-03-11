package org.cloud.mongo;

import lombok.Data;

import java.util.Objects;

/**
 * mongo db查询VO
 */
@Data
public class MongoQueryParam {

    private String name;
    private MongoEnumVO.MongoOperatorEnum operator;
    private MongoEnumVO.DataType dataType;
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
