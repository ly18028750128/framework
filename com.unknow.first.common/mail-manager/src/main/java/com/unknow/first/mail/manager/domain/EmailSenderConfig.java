package com.unknow.first.mail.manager.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2022-12-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_email_sender_config")
@ApiModel(value="EmailSenderConfig对象", description="")
public class EmailSenderConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "email_sender_id", type = IdType.AUTO)
    private Integer emailSenderId;

    @ApiModelProperty(value = "邮箱用户名")
    private String userName;

    @ApiModelProperty(value = "邮箱密码：aes加密")
    private String password;

    @ApiModelProperty(value = "邮件发送服务器")
    private String host;

    @ApiModelProperty(value = "邮件端口，默认为SSL的端口465")
    private Integer port;

    @ApiModelProperty(value = "协议，默认为SMTP")
    private String protocol;

    @ApiModelProperty(value = "utf-8")
    private String defaultEncoding;

    @ApiModelProperty(value = "开启SSL，默认开启")
    private Boolean tlsEnabled;

    @ApiModelProperty(value = "状态")
    private Boolean status;


    @ApiModelProperty(value = "开启STARTTLS")
    private Boolean starttlsEnabled;

    private String createdBy;

    private LocalDateTime createdDate;

    private String updatedBy;

    private LocalDateTime updatedDate;


}
