package com.longyou.comm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.cloud.mongo.MongoEnumVO.DataType;
import org.cloud.mongo.MongoEnumVO.MongoOperatorEnum;
import org.cloud.mongo.annotation.MongoQuery;

@ApiModel("定时任务日志查询DTO")
@Data
public class JobLogQueryDTO {

    @ApiModelProperty(value = "执行结果(success/fail)", example = "success")
    private String executeResult;

    @ApiModelProperty(value = "服务名称", example = "commonService")
    private String serviceName;

    @ApiModelProperty(value = "任务名称")
    private String jobName;

    @ApiModelProperty(value = "执行时间")
    @MongoQuery(dateType = DataType.UNIX_TIMESTAMP,operator = MongoOperatorEnum.BETWEEN)
    private List<Long> fireTime;

    @ApiModelProperty(value = "日志创建日期")
    @MongoQuery(dateType = DataType.UNIX_TIMESTAMP,operator = MongoOperatorEnum.BETWEEN)
    private List<Long> createDate;
}
