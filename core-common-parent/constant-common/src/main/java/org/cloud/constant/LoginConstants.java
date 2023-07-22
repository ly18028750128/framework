package org.cloud.constant;

import lombok.AllArgsConstructor;

public final class LoginConstants {


    private LoginConstants() {
    }

    /**
     * 后台管理用户的注册来源，对应t_frame_work.user_regist_source字段，其它的来源为各个微服务的id,如CommonService等其它的微服务appname;
     */
    public final static String REGIST_SOURCE_BACKGROUND = "background";

    /**
     * 登录方式配置，t_frame_work.user_type保持一致，数据字典的key对应此值
     */
    @AllArgsConstructor
    public static enum UserType {
        ADMIN("admin", "后台管理用户", "后台管理用户"), // 后台添加是默认为此用户
        THIRD_LOGIN("LOGIN-BY-THIRD-LOGIN", "第三方登录校验，仅生成用户和返回token，校验过程在第三方实现", "第三方登录校验，仅生成用户和返回token，校验过程在第三方实现"),
        WEIXIN_MICROAPP("LOGIN-BY-WEIXIN-MICROAPP", "微信小程序登录", "微信小程序登录"),
        WEIXIN_SCANN_CODE("LOGIN-BY-WEIXIN-SCANN-CODE", "微信扫码登录", "微信扫码登录"),
        ;

        public final String value;     // 值
        public final String name;  // 名称和描述
        public final String i18nValue;  // 国际化
    }


    @AllArgsConstructor
    public enum LoginError {
        IP_LOCK_KEY("SYSTEM:ERROR:LOGIN:IP:LOCKER:", "ip地址错误登录锁", "system.error.login.ip.locked"),
        USER_LOCK_KEY("SYSTEM:ERROR:LOGIN:USER:LOCKER:", "ip地址错误登录锁", "system.error.login.user.locked"),
        IP_ERROR_COUNT_KEY("SYSTEM:ERROR:LOGIN:IP:ERROR:COUNT:", "ip地址登录错误计数", "system.error.login.ip.count"),
        USER_ERROR_COUNT_KEY("SYSTEM:ERROR:LOGIN:USER:ERROR:COUNT:", "用户登录错误计数", "system.error.login.user.count"),
        ;
        public final String value;     // 值
        public final String name;  // 名称和描述
        public final String i18nValue;  // 国际化
    }

}
