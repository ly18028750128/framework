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

    public final static String _MICRO_LOGIN_CODE_KEY = "micrLoginCode";

    public final static String _MICRO_APPINDEX_KEY = "microAppindex";

    public final static String _MICRO_APPNAME_KEY = "microAppName";

    public final static String _USER_TYPE_KEY = "userType";

    public final static String _USER_TYPE_DEFAULT_VALUE = "admin";


}
