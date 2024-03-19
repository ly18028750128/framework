package com.cloud.parammanage.common.constants;

public interface ParamConstants {

    String name();
    String getDesc();

    Class<?> getCls();

    <T> T getDefaultValue();
}
