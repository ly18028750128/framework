package com.longyou.comm;

import org.cloud.constant.BasicEnum;

public final class CommonServiceConst {

    private CommonServiceConst() {

    }

    public static enum userStatus implements BasicEnum {

        Active(1,"生效","active"),
        Inactive(-1,"失效","active"),
        New(2,"新建","active"),
        Reset(3,"重置","active"),
        ;

        userStatus(Integer value, String description, String i18nValue) {
            this.value = value;
            this.description = description;
            this.i18nValue = i18nValue;
        }

        private Integer value;
        private String description;
        private String i18nValue;

        @Override
        public Integer value() {
            return value;
        }

        @Override
        public String description() {
            return description;
        }

        @Override
        public String i18nValue() {
            return i18nValue;
        }
    }

}
