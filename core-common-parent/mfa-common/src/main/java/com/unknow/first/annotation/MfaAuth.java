package com.unknow.first.annotation;

import org.cloud.constant.CoreConstant;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MfaAuth {
    CoreConstant.MfaAuthType mfaAuthType() default CoreConstant.MfaAuthType.GOOGLE;
}
