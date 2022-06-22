package com.longyou.gateway.constants;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LoginFormField {

    PASSWORD("password", "密码"),
    USER_NAME("userName", "用户名"),
    VALIDATE_CODE("validateCode", "用户输入的验证码"),
    VALIDATE_REDIS_KEY("validateRedisKey", "保存验证码的redis key"),
    OTHER_FIELD_KEY("otherField", "表单提交的其它值，格式为json字符串，如{}"),
    ;

    public final String field;   // 表单字段名称
    public final String description; // 表单字段描述


}
