package com.unknow.first.mail.manager.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.Getter;

@Data
@ApiModel("邮件发送VO")
public class MailVO {

    /**
     * 发送人, 如果没有, 取全局配置的发送人;
     */
    @ApiModelProperty("发送人")
    private String from;
    @ApiModelProperty("接收地址")
    private List<String> to = new ArrayList<>(); // 发送给某人
    @ApiModelProperty("主题，可加参数，示例{{param1}}")
    private String subject; // 主题
    @ApiModelProperty("内容")
    private String text; // 内容
    @ApiModelProperty("抄送")
    private List<String> cc = new ArrayList<>(); // 抄送
    @ApiModelProperty("密送")
    private List<String> bcc = new ArrayList<>(); // 密送
    @ApiModelProperty("发送时间")
    private Date sendDate;
    @ApiModelProperty("thymeleaf模板")
    private String templateText; //thymeleaf模板
    @ApiModelProperty("邮件参数")
    private EmailParams params = new EmailParams();
    @ApiModelProperty("附件，暂时没有用到")
    private List<File> files;  // 附件
    @ApiModelProperty("资源ID，发送文件时会用到，现在暂时没用")
    private String[] resId; // 资源ID

    @Getter
    @ApiModel("邮件参数")
    public static class EmailParams {
        @ApiModelProperty("标题参数，示例：{{param1}}")
        final private Map<String, Object> emailParams = new HashMap<>(10);
        @ApiModelProperty("标题参数，请参考thymeleaf")
        final private Map<String, String> subjectParams = new HashMap<>(10);
    }

}
