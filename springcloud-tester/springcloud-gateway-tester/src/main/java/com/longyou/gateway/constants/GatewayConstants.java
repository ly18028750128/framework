package com.longyou.gateway.constants;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

public class GatewayConstants {

    @AllArgsConstructor
    public enum LoginException {
        ERROR_10(-10, "microServiceName不能为空", "gateway.login.exception.microServiceName.isnull"),
        ERROR_20(-20, "密码不能为空", "gateway.login.exception.password.isnull"),
        ERROR_30(-30, "用户登录验证失败次数过多，用户被锁定", "gateway.loginError.user.havenLocked"),
        ERROR_35(-35, "当前IP登录验证失败次数过多，用户被锁定", "gateway.loginError.ip.havenLocked"),
        ERROR_40(-40, "验证码错误", "gateway.loginError.user.validateCodeError"),

        ;
        public final int value;     // 值
        public final String desc;  // 名称和描述
        public final String i18nValue;  // 国际化
    }
}
