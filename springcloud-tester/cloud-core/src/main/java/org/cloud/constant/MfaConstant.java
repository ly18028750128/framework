package org.cloud.constant;

public enum MfaConstant implements BasicEnum<String> {

    MFA_HEADER_NAME("x-header-mfa-value", "mfa验证的request请求头的值", "system.mfa.header.name"),
    CORRELATION_YOUR_GOOGLE_KEY("system.mfa.refer.your.google.key", "请绑定您的谷歌验证的Secret", "system.mfa.refer.your.google.key"),
    CORRELATION_GOOGLE_VERIFY_CODE_ISNULL("system.mfa.google.code.isnull", "谷歌验证码为空，请输入谷歌验证码", "system.mfa.google.code.isnull"),
    CORRELATION_GOOGLE_VERIFY_CODE_NOT_NUMERIC("system.mfa.google.code.is.not.numeric", "谷歌验证码应该为数字，请输入正确谷歌验证码", "system.mfa.google.code.is.not.numeric"),
    CORRELATION_GOOGLE_VERIFY_FAILED("system.mfa.google.verify.failed", "谷歌验证失败，请输入正确谷歌验证码", "system.mfa.google.verify.failed"),
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

    public final static String _GOOGLE_MFA_USER_SECRET_REF_ATTR_NAME = "GOOGLE_MFA_USER_SECRET_REF_ATTR_NAME"; // 谷歌密钥存储在user_ref表里的属性名称
}
