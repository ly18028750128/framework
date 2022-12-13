package com.unknow.first.mail.manager.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName t_email_template
 */
@TableName(value ="t_email_template")
@Data
@ApiModel("邮件模板信息")
public class EmailTemplate implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("模板ID")
    private Long templateId;

    /**
     * 模板编码
     */
    @ApiModelProperty("templateCode")
    private String templateCode;

    /**
     * 语言，默认zh_CN
     */
    @ApiModelProperty("语言，默认zh_CN")
    private String language;

    /**
     * 主题（变量用{{变量名}}，方便替换）
     */
    @ApiModelProperty("主题（变量用{{变量名}}，方便替换）")
    private String subject;

    /**
     * 默认发送人(需要经过校验，暂时全局只有一个发送者，也就是配置的发送者)
     */
    @ApiModelProperty(value = "发送人：从邮件配置中获取，为空时从spring默认配置的发送者发送")
    private String fromAddress;

    /**
     * 默认接收人（多个请用,号分割）
     */
    @ApiModelProperty(value = "默认接收人（多个请用,号分割）")
    private String toAddress;

    /**
     * 模板内容（thymeleaf模板）
     */
    @ApiModelProperty(value = "模板内容（thymeleaf模板）")
    private String templateText;

    /**
     * 抄送
     */
    @ApiModelProperty(value = "抄送")
    private String cc;

    /**
     * 密送
     */
    @ApiModelProperty(value = "密送")
    private String bcc;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date createdDate;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private Date updatedDate;

    @TableField(exist = false)
    private static final long serialVersionUID = 154656498113271L;
}