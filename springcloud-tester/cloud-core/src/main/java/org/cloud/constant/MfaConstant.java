package org.cloud.constant;

public enum MfaConstant implements BasicEnum<String> {

    MFA_HEADER_NAME ("x-header-mfa-value", "mfa验证的request请求头的值", "system.mfa.header.name"),
    CORRELATION_YOUR_GOOGLE_KEY("system.mfa.refer.your.google.key", "请关联您的谷歌验证的Key", "system.mfa.refer.your.google.key"),
    ;

    private String value;
    private String description;
    private String i18nValue;

    MfaConstant(String value, String description, String i18nValue) {
        this.value = value;
        this.description = description;
        this.i18nValue = i18nValue;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String description() {
        return description;
    }

    @Override
    public String i18nValue() {
        return i18nValue;
    }
}
