package org.cloud.constant;

public interface BasicEnum<T> {

    public <T> T value();
    public String description();
    public String i18nValue();
}
