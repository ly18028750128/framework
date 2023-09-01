package com.unknow.first.api.encrypt.annotation;

import com.unknow.first.api.encrypt.ApiEncryptType;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.stereotype.Indexed;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface ApiEncrypt {

    //加密方式
    ApiEncryptType encryptType() default ApiEncryptType.BY_SYSTEM;

}
