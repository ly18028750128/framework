package org.cloud.annotation;

import org.cloud.constant.CoreConstant;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MfaAuth {
    CoreConstant.mfaAutoType mfaAutoType() default  CoreConstant.mfaAutoType.GOOGLE;
}
