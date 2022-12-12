package org.cloud.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel("消息发送日志")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageLogVO {

    @ApiModelProperty("服务名称")
    private String serviceName;

    @ApiModelProperty(value = "消息类型", notes = "SMS/EMAIL/其它")
    private String type;

    @ApiModelProperty(value = "发送者")
    private String sender;

    @ApiModelProperty(value = "模板名称")
    private String templateCode;

    @ApiModelProperty("主题")
    private String subject; // 主题

    @ApiModelProperty("消息内容")
    private String content;

    @ApiModelProperty("接收者")
    private String to; // 发送给某人

    @ApiModelProperty("抄送,邮件才有值")
    private String cc; // 抄送

    @ApiModelProperty("密送,邮件才有值")
    private String bcc; // 密送

    @ApiModelProperty(value = "发送结果", notes = "success/fail")
    private String result;

    @ApiModelProperty("发送日期")
    private Date sendDate;

}
