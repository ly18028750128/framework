package com.unknow.first.mongo.vo;

import com.unknow.first.mongo.vo.MongoEnumVO.DataType;
import com.unknow.first.mongo.vo.MongoEnumVO.MongoOperatorEnum;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MongoQuery {

    // Dong ZhaoYang 2017/8/7 基本对象的属性名
    String propName() default "";

    // Dong ZhaoYang 2017/8/7 查询方式
    MongoOperatorEnum operator() default MongoOperatorEnum.IS;

    DataType dateType() default DataType.String;
}
