package org.cloud.constant;

import java.text.SimpleDateFormat;

public final class CoreConstant {

    private CoreConstant() {
    }

    public static enum DateTimeFormat {
        FULLDATE("yyyy-MM-dd"),FULLDATETIME("yyyy-MM-dd hh:mm:ss"),MonthAndDay("MM-dd");

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
    public static enum RestStatus implements BasicEnum{

        SUCCESS(200,"成功","rest.status.200"),
        NOAUTH(401,"未授权","rest.status.401"),
        FAIL(-100,"失败","rest.status.-100"),
        ;

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
        public String description(){
            return this.statusName;
        }
    }

    public final static String _MICRO_LOGIN_CODE_KEY = "micrLoginCode";

    public final static String _MICRO_APPINDEX_KEY = "microAppindex";

    public final static String _MICRO_APPNAME_KEY = "microAppName";

    public final static String _USER_TYPE_KEY = "userType";

    public final static String _USER_TYPE_DEFAULT_VALUE = "admin";

    public final static String _REDIS_USER_SUCCESS_TOKEN_PREFIX = "USER:LOGIN:SUCCESS:NANCE:";

    // 改动后所有的basic64验证将全部失效！！！！,也可以通过system.auth_basic64_split配置覆盖这个值
    public final static String _USER_BASIC64_SPLIT_STR = "adnf56ds34fj232lnz81h7a91d5f43a343n3zb0ga";

}
