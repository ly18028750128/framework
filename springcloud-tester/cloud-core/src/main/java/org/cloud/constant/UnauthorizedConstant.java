package org.cloud.constant;

import lombok.AllArgsConstructor;

/**
 * 无权限状态码类
 */

@AllArgsConstructor
public enum UnauthorizedConstant implements BasicEnum<String> {

    API_UNAUTHORIZED("unauthorized.api", "无api权限"),
    DATA_INTERFACE_UNAUTHORIZED("unauthorized.data.interface", "无数据查询接口权限"),
    ;

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
