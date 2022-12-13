package com.longyou.comm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;
import org.cloud.mongo.MongoEnumVO.DataType;
import org.cloud.mongo.MongoEnumVO.MongoOperatorEnum;
import org.cloud.mongo.annotation.MongoQuery;

@ApiModel("消息日志查询DTO")
@Data
public class MessageLogQueryDTO {

    @ApiModelProperty("服务名称")
    private String serviceName;

    @ApiModelProperty(value = "消息类型(SMS/EMAIL/其它)", example = "EMAIL")
    private String type;

    @ApiModelProperty(value = "发送者")
    private String sender;

    @ApiModelProperty("接收者")
    private String to; // 发送给某人

    @ApiModelProperty(value = "模板名称")
    private String templateCode;

    @ApiModelProperty("主题")
    @MongoQuery(operator = MongoOperatorEnum.REGEX)
    private String subject; // 主题

    @ApiModelProperty("消息内容")
    @MongoQuery(operator = MongoOperatorEnum.REGEX)
    private String content;

    @ApiModelProperty(value = "发送结果(success/fail)", example = "success")
    private String result;

    @ApiModelProperty(value = "发送日期,格式：yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",example = "2022-12-12T00:00:00.000Z,2022-12-13T00:00:00.000Z")
    @MongoQuery(dateType = DataType.Date,operator = MongoOperatorEnum.BETWEEN)
    private List<String> sendDate;
}
