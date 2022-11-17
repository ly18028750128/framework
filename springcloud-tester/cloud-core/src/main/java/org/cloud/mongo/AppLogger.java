package org.cloud.mongo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * 日志类
 */
@Data
@ApiModel("日志类")
public class AppLogger implements Serializable {

    @ApiModelProperty("微服务名称")
    private String microServiceName; // 微服务名称
    @ApiModelProperty("日志类型")
    private String type;   // 日志类型
    @ApiModelProperty("执行方法")
    private String method; // 执行方法
    @ApiModelProperty("入参")
    private String[] params; // 入参
    @ApiModelProperty("结果")
    private String result;  // 结果
    @ApiModelProperty("类名")
    private String className; // 类名
    @ApiModelProperty("异常信息")
    private String exceptionStr; // 异常信息
}
