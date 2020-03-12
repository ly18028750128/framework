package org.cloud.vo;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class DynamicSqlQueryParamsVO {

    private Map<String,Object> params = new LinkedHashMap<>();   // 参数
    private String sorts;  // 排序字段

}
