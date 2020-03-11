package org.cloud.mongo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.cloud.utils.mongo.MongoDBEnum;

import java.util.Objects;

@Data
@NoArgsConstructor
public class DataInterFaceParamVO {
    private int seq; //序号
    private String fieldName;   // 字段名称
    private MongoEnumVO.DataType fieldType;   // 数据类型
    private String description; // 描述
    private int status; //状态，0失效，1有效

    public DataInterFaceParamVO(String fieldName, MongoEnumVO.DataType fieldType, String description,int status) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.description = description;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        DataInterFaceParamVO that = (DataInterFaceParamVO) o;
        return fieldName.equals(that.fieldName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldName);
    }
}