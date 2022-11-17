package org.cloud.mongo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;

public final class MongoEnumVO {

    /**
     * 运算符
     */
    @ApiModel("mongo操作符定义")
    @AllArgsConstructor
    public enum MongoOperatorEnum {
        GTE("gte", "大于等于"),
        LTE("lte", "小于等于"),
        GT("gt", "大于"),
        LT("lt", "小于"),
        BETWEEN("between", "在一定范围内"),
        IS("is", "等于"),
        IN("in", "in"),
        NIN("nin", "not in"),
        REGEX("regex", "正则表达式"),
        NE("ne", "不等于"),
        ALL("all", "全等于"),
        EXISTS("exists", "存在字段"),
        type("type", "类型"),  // int型，参考mongodb的字段编码
        ELEMMATCH("elemMatch", "按行匹配，如果数据有多行"),  // int型，参考mongodb的字段编码
        ;
        @ApiModelProperty("操作符名称")
        public final String name;
        @ApiModelProperty("操作符描述")
        public final String description;

    }

    /**
     * 关系运算符，先定义着，暂时不支持
     */
    @ApiModel("条件连接方式")
    @AllArgsConstructor
    public enum RelationalOperator {
        AND("and", "并且"),
        OR("or", "或者"),
        NOR("nor", "或者取反");

        @ApiModelProperty("连接方式名称")
        public final String name;
        @ApiModelProperty("连接方式描述")
        public final String description;
    }

    /**
     * 数据类型
     */
    @ApiModel("数据类型")
    @AllArgsConstructor
    public enum DataType {
        Double("Double", 1, ""),
        String("String", 2, ""),
        Object("Object", 3, ""),
        Array("Array", 4, ""),
        BinaryData("Binary data", 5, ""),
        Undefined("Undefined", 6, "已废弃。"),
        ObjectId("Object id", 7, ""),
        Boolean("Boolean", 8, ""),
        Date("Date", 9, ""),
        Null("Null", 10, ""),
        RegularExpression("Regular Expression", 11, "正则表达式"),
        JavaScript("JavaScript", 13, ""),
        Symbol("Symbol", 14, ""),
        JavaScriptScope("JavaScript (with scope)", 15, ""),
        Integer("32-bit integer", 16, ""),
        Timestamp("Timestamp", 17, ""),
        Long("64-bit integer", 18, ""),
        MinKey("Min key", 255, "Query with -1."),
        MaxKey("Max key", 127, "");

        @ApiModelProperty("类型名称")
        public final String name;
        @ApiModelProperty("类型Id，对应mongo的id")
        public final int id;
        @ApiModelProperty("类型描述")
        public final String description;
    }


}
