package com.cloud.parammanage.common.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface ParamFieldConstants {
    /**
     * 参数配置类型
     */
    @Getter
    @AllArgsConstructor
    public static enum ConfigType {
        PRIVATE(10, "私有"),
        PUBLIC(20, "公开")
        ;

        private int value;
        private String name;
    }
}
