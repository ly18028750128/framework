package org.cloud.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.text.SimpleDateFormat;

public final class CoreConstant {

    public final static String RSA_KEYS_REDIS_KEY = "SYSTEM:CONFIG:RSA_KEYS";

    private CoreConstant() {
    }

    public static enum DateTimeFormat {
        FULLDATE("yyyy-MM-dd"), FULLDATETIME("yyyy-MM-dd HH:mm:ss"), MonthAndDay("MM-dd"), ISODATE("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        DateTimeFormat(String value) {
            this.value = value;
            this.dateFormat = new SimpleDateFormat(value);
        }

        private String value;
        private SimpleDateFormat dateFormat;

        public String getValue() {
            return value;
        }

        public SimpleDateFormat getDateFormat() {
            return dateFormat;
        }
    }

    // rest服务返回值处理，可以继续增加
    public static enum RestStatus implements BasicEnum {

        SUCCESS(200, "成功", "rest.status.200"), NOAUTH(401, "未授权", "rest.status.401"), PARTSUCCESS(100, "部分成功", "rest.status.100"), FAIL(
            -100, "失败", "rest.status.-100");

        private int statusValue;     // 值
        private String statusName;  // 名称和描述
        private String i18nValue;  // 国际化

        RestStatus(int statusValue, String statusName, String i18nValue) {
            this.statusValue = statusValue;
            this.statusName = statusName;
            this.i18nValue = i18nValue;
        }

        @Override
        public Integer value() {
            return this.statusValue;
        }

        @Override
        public String i18nValue() {
            return i18nValue;
        }

        @Override
        public String description() {
            return this.statusName;
        }
    }

    public final static String _MICRO_LOGIN_CODE_KEY = "micrLoginCode";

    public final static String _MICRO_APPINDEX_KEY = "microAppindex";


    public final static String _MICRO_APPNAME_KEY = "microAppName";
    public final static String _USER_TYPE_KEY = "userType";
    public final static String _USER_TYPE_DEFAULT_VALUE = "admin";
    public final static String _REDIS_USER_SUCCESS_TOKEN_PREFIX = "USER:LOGIN:SUCCESS:CACHE:NANCE:";
    public final static String _BASIC64_TOKEN_USER_CACHE_KEY = "USER:LOGIN:SUCCESS:CACHE:USER:"; // 登录成功后的用户信息
    public final static String _BASIC64_TOKEN_USER_SUCCESS_TOKEN_KEY = "USER:LOGIN:SUCCESS:CACHE:USER:TOKEN:"; // 记录用户登录成功后的所有token值
    public final static String USER_LOGIN_SUCCESS_CACHE_KEY = "USER:LOGIN:SUCCESS:CACHE:"; // 用户的详细信息按id缓存,包含用户权限点等信息。

    // rest服务返回值处理，可以继续增加
    public static enum UserCacheKey implements BasicEnum {

        DATA("data", "数据权限", ""), FUNCTION("function", "功能权限", ""), MENU("menu", "菜单权限", ""), DATA_INTERFACE("dataInterface", "数据接口权限",
            ""), ROLE("role", "角色", ""), ROLE_NAME("roleName", "角色名列表", ""),
        ;

        private String key;     // 值
        private String statusName;  // 名称和描述
        private String i18nValue;  // 国际化

        UserCacheKey(String key, String statusName, String i18nValue) {
            this.key = key;
            this.statusName = statusName;
            this.i18nValue = i18nValue;
        }

        @Override
        public String value() {
            return this.key;
        }

        @Override
        public String i18nValue() {
            return i18nValue;
        }

        @Override
        public String description() {
            return this.statusName;
        }
    }


    // 双因子校验方式，默认为GOOGLE，验证，
    public static enum MfaAuthType implements BasicEnum {

        GOOGLE("GOOGLE", "谷歌验证码", ""), SMS("SMS", "短信验证码", "");

        private String key;     // 值
        private String statusName;  // 名称和描述
        private String i18nValue;  // 国际化

        MfaAuthType(String key, String statusName, String i18nValue) {
            this.key = key;
            this.statusName = statusName;
            this.i18nValue = i18nValue;
        }

        @Override
        public String value() {
            return this.key;
        }

        @Override
        public String i18nValue() {
            return i18nValue;
        }

        @Override
        public String description() {
            return this.statusName;
        }
    }

    // 改动后所有的basic64验证将全部失效！！！！,也可以通过system.auth_basic64_split配置覆盖这个值
    public final static String _USER_BASIC64_SPLIT_STR = "%a1b2c0k3d4y8%";


    /**
     * 用户权限验证方式
     */
    public static enum AuthMethod implements BasicEnum {

        ALLSYSTEMUSER("ALLSYSTEMUSER", "所有登录用户", "rest.AuthMethod.ALLSYSTEMUSER"), BYUSERPERMISSION("BYUSERPERMISSION", "通过用户所属权限验证",
            "rest.AuthMethod.BYUSERPERMISSION"), NOAUTH("NOAUTH", "不用授权", "rest.AuthMethod.NOAUTH"),
        ;

        private String value;     // 值
        private String name;  // 名称和描述
        private String i18nValue;  // 国际化

        AuthMethod(String value, String name, String i18nValue) {
            this.value = value;
            this.name = name;
            this.i18nValue = i18nValue;
        }

        @Override
        public String value() {
            return this.value;
        }

        @Override
        public String i18nValue() {
            return i18nValue;
        }

        @Override
        public String description() {
            return this.name;
        }
    }

    /**
     * 数据范围类型
     */
    public static enum DataAutoType implements BasicEnum {

        MICROSERVICE("micro", "微服务", ""), ORGANIZATION("org", "组织机构（部门）", ""),
        ;

        private String value;     // 值
        private String name;  // 名称和描述
        private String i18nValue;  // 国际化

        DataAutoType(String value, String name, String i18nValue) {
            this.value = value;
            this.name = name;
            this.i18nValue = i18nValue;
        }

        @Override
        public String value() {
            return this.value;
        }

        @Override
        public String i18nValue() {
            return i18nValue;
        }

        @Override
        public String description() {
            return this.name;
        }
    }

    public final static String _FUNCTION_SPLIT_STR = "::";

    public static enum MongoDbLogConfig {
        MONGODB_LOG_SUFFIX("_logbackLogCollection", "日志存储collection后缀"), MONGODB_OPERATE_LOG_SUFFIX("_logbackOperateLogCollection",
            "操作日志存储collection后缀"), CREATE_DATE_FIELD("createDate", "创建日期字段");
        private String value;
        private String desc;

        MongoDbLogConfig(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }

        public String value() {
            return value;
        }
    }


    public final static String _SYSTEM_DIC_CACHE_KEY = "system:admin:dic:";

    public final static String _SYSTEM_DIC_ITEMS_CACHE_KEY = "dicItems";
    public final static String _SYSTEM_DIC_ITEMS_CACHE_KEY_WHIT_DOT = "." + _SYSTEM_DIC_ITEMS_CACHE_KEY;

    public final static String _GENERAL_SYSDIC_NAME = "General";  // 系统通用的数据字典的key值


    public static enum SystemSupportLanguage implements BasicEnum {

        ZH_CN("zh_CN", "中文"), EN_US("en_US", "English");

        private String code;
        private String name;

        SystemSupportLanguage(String code, String name) {
            this.code = code;
            this.name = name;
        }

        @Override
        public String value() {
            return this.code;
        }

        @Override
        public String i18nValue() {
            return this.name;
        }

        @Override
        public String description() {
            return this.name;
        }
    }

    /**
     * 操作日志类型
     */
    @Getter
    @AllArgsConstructor
    public enum OperateLogType implements BasicEnum {

        LOG_TYPE_DEFAULT(0, "默认"),

        LOG_TYPE_FRONTEND(1, "前端操作"),

        LOG_TYPE_BACKEND(2, "后台管理操作"),

        LOG_TYPE_SCHEDULER(3, "定时任务调用"),
        ;

        private int logType;

        private String logTypeDesc;

        @Override
        public Object value() {
            return logType;
        }

        @Override
        public String description() {
            return logTypeDesc;
        }

        @Override
        public String i18nValue() {
            return logTypeDesc;
        }
    }

    public final static String _VALIDATE_CODE_CACHE_KEY_PREFIX = "validate:code:values:";
}
