package org.cloud.annotation;

import org.cloud.constant.CoreConstant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 操作日志注解类
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD) //只能注解到方法
public @interface AuthLog {
	
	//业务类型
	String bizType();
	//接口描述
	String desc();

	CoreConstant.OperateLogType operateLogType() default CoreConstant.OperateLogType.LOG_TYPE_DEFAULT;
}
