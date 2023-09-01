package com.unknow.first.api.encrypt;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ApiEncryptType {
    BY_SYSTEM(1, "用系统的AES统一加密"),
    BY_USER(2, "使用用户登录后生成的密钥加密"),
    ;
    public final Integer type;
    public final String desc;
}
