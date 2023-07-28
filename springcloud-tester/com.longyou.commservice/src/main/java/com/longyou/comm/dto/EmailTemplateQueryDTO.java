package com.longyou.comm.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;
import org.cloud.mybatisplus.annotation.Query;
import org.cloud.mybatisplus.annotation.Query.Type;

/**
 * 
 * @TableName t_email_template
 */

@Data
@ApiModel("邮件模板信息查询DTO")
public class EmailTemplateQueryDTO implements Serializable {


    /**
     * 模板编码
     */
    @ApiModelProperty(value = "模板编码")
    private String templateCode;

    /**
     * 语言，默认zh_CN
     */
    @ApiModelProperty(value = "语言，默认zh_CN")
    private String language;

    /**
     * 主题（变量用{{变量名}}，方便替换）
     */
    @ApiModelProperty(value = "主题（变量用{{变量名}}，方便替换）")
    @Query(type = Type.INNER_LIKE)
    private String subject;

    /**
     * 默认接收人（多个请用,号分割）
     */
    @ApiModelProperty(value = "默认接收人（多个请用,号分割）")
    @Query(type = Type.INNER_LIKE)
    private String toAddress;

    /**
     * 模板内容（thymeleaf模板）
     */
    @ApiModelProperty(value = "模板内容（thymeleaf模板）")
    @Query(type = Type.INNER_LIKE)
    private String templateText;


}