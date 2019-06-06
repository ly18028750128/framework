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

}
