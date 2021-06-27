package org.cloud.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DataDimensionCondition {

  private String fieldName;   //字段名称
  private String dimensionName; //数据维度名称
  private String operator="="; //操作符 如like，默认为=
  private String connector="and"; // 连接符，默认为and
  private String jdbcType="VARCHAR"; // 数据类型，默认为VARCHAR
}
