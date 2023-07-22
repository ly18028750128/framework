package com.unknow.first.api.common;

/**
 * 枚举了一些常用API操作码
 */
public enum ResultCode implements IErrorCode {


    //成功
    SUCCESS(200, "success"),
    //系统错误
    FAILED(1001, "failed"),
    //系统繁忙
    STATUS_ERRORCODE_SYSTEM_BUSY(1002, "errorcode.system.busy"),
    //用户没有权限
    FORBIDDEN(1003, "errorcode.user.nopermissions"),
    //参数不能为空
    PARAMETER_CANNOTBENULL(1004, "errorcode.parameter.cannotbenull"),
    //参数无效
    PARAMETER_INVALIDPARAM(1005, "parameter.invalidparam"),
    //用户未登录
    NOT_LOGIN(1006, "errorcode.user.notlogin"),
    //密码错误
    PASSWORD_ERROR(1007, "errorcode.user.passerror"),
    //密码不能为空
    PASSWORD_NOTFOUND(1008, "error.user.password.notfound"),
    //旧密码不能为空
    OLDPASSWORD_NOTFOUND(1009, "error.user.oldpassword.notfount"),
    //旧密码错误
    OLDPASSWORD_ERROR(1010, "error.user.oldpassword.error"),
    //交易密码为6-30位，必须包含字母和数字
    PASSWORD_FORMAT_ERROR(1011, "error.user.password.format.error"),
    //用户名已被注册
    USERNAME_REGISTERED(1012, "error.user.username.registered"),
    //邮箱不能为空
    EMAIL_NOTFOUNT(1013, "error.user.email.notfount"),
    //邮箱输入有误
    EMAIL_ERROR(1014, "error.user.email.error"),
    //用户名不可输入特殊字符
    USERNAME_FORMAT_ERROR(1015, "error.user.username.format.error"),

    /**
     * 输入不能包含特殊字符
     */
    PARAM_NOT_SPECIALCHAR(1016, "errorcode.param.not.specialchar"),

    /**
     * 用户没有绑定银行卡
     */
    USER_NOT_BIND_BANK(1017, "error.user.notbindbank"),
    /**
     * 用户状态无效
     */
    USER_INVALIDSTATUS(1018, "errorcode.user.invalidstatus"),

    /**
     * 用户操作太快
     */
    USER_OPERATING_TOOFAST(1019, "errorcode.user.operatingtoofast"),

    /**
     * 账户冻结,无法操作
     */
    ACCOUNT_STATUS_FREEZE(1020, "errorcode.finan.accountfree"),
    /**
     * 账户繁忙,稍后再试
     */
    ACCOUNT_OPERATION_BUSY(1021, "errorcode.finan.accountbusy"),
    /**
     * 账户冻结余额不足
     */
    ACCOUNT_FREEZE_NOT_ENOUGH(1022, "errorcode.finan.freezenotenough"),
    /**
     * 账户余额不足
     */
    ACCOUNT_BALANCE_NOT_ENOUGH(1023, "errorcode.finan.balancenotenough"),
    /**
     * 暂停充值
     */
    RECHARGE_CLOSE(1024, "errorcode.finan.cannotrecharge"),
    /**
     * 暂停提现
     */
    WITHDRAWALS_CLOSE(1025, "errorcode.finan.cannotwithdrawal"),
    /**
     * 钱包服务繁忙，请稍后再试
     */
    WALLET_BUSY(1026, "errorcode.wallet.servicebusy"),
    /**
     * 无效币种
     */
    STATUS_ERRORCODE_FINAN_INVALIDCOIN(1027, "errorcode.finan.invalidcoin"),
    /**
     * 手机号码输入有误
     */
    MOBILE_NOTFOUNT_OR_FORMAT_ERROR(1028, "手机号码为空或格式错误"),

    /**
     * 验证码已失效
     */
    STATUS_ERRORCODE_USER_SMSHAVEEXPIRED(1029, "验证码已失效"),

    /**
     * 获取验证码冷却中
     */
    STATUS_ERRORCODE_USER_SMSINTHECOOLING(1030, "获取验证码冷却中"),

    /**
     * 获取验证码达到日上限
     */
    STATUS_ERRORCODE_USER_SMSDAYSENDLIMIT(1031, "获取验证码达到日上限"),


    ;
    private long code;
    private String message;

    private ResultCode(long code, String message) {
        this.code = code;
        this.message = message;
    }

    public long getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
