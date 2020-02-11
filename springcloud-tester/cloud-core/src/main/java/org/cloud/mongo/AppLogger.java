package org.cloud.mongo;

import lombok.Data;

import java.io.Serializable;

/**
 * 日志类
 */
@Data
public class AppLogger implements Serializable {
    private String method;
    private String[] params;
    private String className;
    private String exceptionStr;
}
