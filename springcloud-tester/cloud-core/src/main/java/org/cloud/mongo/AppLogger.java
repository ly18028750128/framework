package org.cloud.mongo;

import lombok.Data;

import java.io.Serializable;

/**
 * 日志类
 */
@Data
public class AppLogger implements Serializable {
    private String microServiceName; // 微服务名称
    private String type;   // 日志类型
    private String method; // 执行方法
    private String[] params; // 入参
    private String result;  // 结果
    private String className; // 类名
    private String exceptionStr; // 异常信息
}
