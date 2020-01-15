package org.cloud.annotation;

import org.cloud.constant.CoreConstant;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 系统资源注解
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface SystemResource {

    String menuName() default "";    // 菜单名称，可选，如果不为空，那么会自动创建菜单
    String menuCode() default "";    // 菜单编码，可选，如果不为空，那么会自动创建菜单
    String parentMenuCode() default ""; // 父级菜单名称，默认为无上级
    String parentMenuName() default ""; // 父级菜单名称，默认为无上级
    String path() default "";  //资源路径，用于分类资源,例如 商品管理/SPU管理

    int index() default 0;  // 序号
    String description() default "";     // 资源名称，注解在方法上时会自动将菜单资源挂在菜单下面
    String value() default "";     // 资源名称，注解在方法上时会自动将菜单资源挂在菜单下面
    CoreConstant.AuthMethodMethod authMethod() default CoreConstant.AuthMethodMethod.ALLSYSTEMUSER;   //验证方式,默认为所有的登录用户,ByUserAuth
}
