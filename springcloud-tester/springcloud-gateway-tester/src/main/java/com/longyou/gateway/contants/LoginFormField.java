package com.longyou.gateway.contants;

public enum LoginFormField {

    PASSWORD("password", "密码"),
    USER_NAME("userName", "用户名"),
    VALIDATE_CODE("validateCode", "用户输入的验证码"),
    VALIDATE_REDIS_KEY("validateRedisKey", "保存验证码的redis key"),
    ;

    private String field;   // 表单字段名称
    private String description; // 表单字段描述

    LoginFormField(String field, String description) {
        this.field = field;
        this.description = description;
    }

    public String field() {
        return field;
    }


    public String description() {
        return description;
    }


}
