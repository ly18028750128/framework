package org.cloud.constant;

import lombok.AllArgsConstructor;

public final class LoginConstants {


    private LoginConstants() {
    }

    /**
     * 后台管理用户的注册来源，对应t_frame_work.user_regist_source字段，其它的来源为各个微服务的id,如CommonService等其它的微服务appname;
     */
    public final static String REGIST_SOURCE_BACKGROUND = "background";

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
