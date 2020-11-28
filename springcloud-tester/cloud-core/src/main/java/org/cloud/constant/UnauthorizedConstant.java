package org.cloud.constant;

/**
 * 无权限状态码类
 */


public enum UnauthorizedConstant implements BasicEnum<String> {

    API_UNAUTHORIZED("unauthorized.api", "无api权限"),
    DATA_INTERFACE_UNAUTHORIZED("unauthorized.data.interface", "无数据查询接口权限"),
    LOGIN_UNAUTHORIZED("unauthorized.login", "用户未登录"),
    ;

    UnauthorizedConstant(String code, String description) {
        this.code = code;
        this.description = description;
    }

    private String code;
    private String description;


    @Override
    public String value() {
        return code;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public String i18nValue() {
        return code;
    }
}
