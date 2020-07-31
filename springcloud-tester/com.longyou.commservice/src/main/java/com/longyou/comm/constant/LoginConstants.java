package com.longyou.comm.constant;

import org.cloud.constant.BasicEnum;

public final class LoginConstants {


    private LoginConstants() {
    }

    /**
     * 登录方式配置，数据字典的key对应此值
     */
    public static enum LoginType implements BasicEnum {

        WEIXIN_MICROAPP("LOGIN-BY-WEIXIN-MICROAPP", "微信小程序登录", "微信小程序登录"),
        WEIXIN_SCANN_CODE("LOGIN-BY-WEIXIN-SCANN-CODE", "微信扫码登录", "微信扫码登录"),
        THIRD_LOGIN("LOGIN-BY-THIRD-LOGIN", "第三方登录校验，仅生成用户和返回token，校验过程在第三方实现", "第三方登录校验，仅生成用户和返回token，校验过程在第三方实现"),
        ;

        private String value;     // 值
        private String name;  // 名称和描述
        private String i18nValue;  // 国际化

        LoginType(String value, String name, String i18nValue) {
            this.value = value;
            this.name = name;
            this.i18nValue = i18nValue;
        }

        @Override
        public String value() {
            return null;
        }

        @Override
        public String description() {
            return null;
        }

        @Override
        public String i18nValue() {
            return null;
        }
    }

}
