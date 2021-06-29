package com.longyou.aihelper.util;

/**
 * 枚举了一些常用API操作码
 * Created by macro on 2019/4/19.
 */
public enum ResultCode implements IErrorCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    NOT_LOGIN(402, "未登录"),
    FORBIDDEN(403, "没有相关权限"),
    VALIDATE_FAILED(404, "参数检验失败"),
    SYSTEMBUY(405,"系统繁忙"),
    TRADE_PASSWORD_ERROR(406,"交易密码错误"),

    ACCOUNT_STATUS_FREEZE(601, "账户冻结,无法操作"),
    ACCOUNT_OPERATION_BUSY(602, "账户繁忙,稍后再试"),
    ACCOUNT_FREEZE_NOT_ENOUGH(603, "账户冻结余额不足"),
    ACCOUNT_BALANCE_NOT_ENOUGH(603, "账户余额不足"),
    RECHARGE_CLOSE(604,"暂停充值"),
    WITHDRAWALS_CLOSE(605,"暂停提现"),
    WALLET_BUSY(606,"钱包服务繁忙，请稍后再试"),
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
