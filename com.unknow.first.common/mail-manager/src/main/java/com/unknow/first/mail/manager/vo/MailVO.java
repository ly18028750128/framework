package com.unknow.first.mail.manager.vo;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class MailVO {

    /**
     * 发送人, 如果没有, 取全局配置的发送人;
     */
    private String from;
    private String[] to; // 发送给某人
    private String subject; // 主题
    private String text; // 内容
    private String[] cc; // 抄送
    private String[] bcc; // 密送
    private Date sendDate;
    private String templateText; //thymeleaf模板
    private Map<String, Object> emailParams;
    private List<File> files;  // 附件
    private String[] resId; // 资源ID
}