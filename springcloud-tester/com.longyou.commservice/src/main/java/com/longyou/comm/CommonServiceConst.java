package com.longyou.comm;

import org.cloud.constant.BasicEnum;

public final class CommonServiceConst {

    private CommonServiceConst() {

    }



    public  enum userStatus implements BasicEnum {

        Active(1, "生效", "active"),
        Inactive(-1, "失效", "active"),
        Disabled(-2, "禁用", "disabled"),
        New(2, "新建", "active"),
        Reset(3, "重置", "active"),
        ;

        userStatus(Integer value, String description, String i18nValue) {
            this.value = value;
            this.description = description;
            this.i18nValue = i18nValue;
        }

        private final Integer value;
        private final String description;
        private final String i18nValue;

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
