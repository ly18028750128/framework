package com.longyou.gateway.security.response;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum MessageCode {
    COMMON_SUCCESS("0000_0", "gateway.login.success"),
    COMMON_FAILURE("0000_1", "gateway.loginError.fail"),
    COMMON_AUTHORIZED_FAILURE("0000_24", "gateway.loginError.fail")
    ;

    //Message 编码
    public final String code;
    //Message 描叙
    public final String message;
}
