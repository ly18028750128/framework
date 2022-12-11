package com.unknow.first.mail.manager.util;


import static constant.MailConstants.MAIL_SENDER_MAP_KEY;

import com.unknow.first.mail.manager.JavaMailSenderSerializable;
import com.unknow.first.mail.manager.domain.EmailSenderConfig;
import com.unknow.first.mail.manager.domain.EmailTemplate;
import com.unknow.first.mail.manager.feign.IEmailTemplateFeignClient;
import com.unknow.first.mail.manager.service.IEmailSenderService;
import com.unknow.first.mail.manager.service.impl.EmailSenderServiceImpl;
import com.unknow.first.mail.manager.vo.MailVO;
import com.unknow.first.mail.manager.vo.MailVO.EmailParams;
import java.util.List;
import java.util.concurrent.Future;
import lombok.extern.slf4j.Slf4j;
import org.cloud.core.redis.RedisUtil;
import org.cloud.exception.BusinessException;
import org.cloud.utils.AES128Util;
import org.cloud.utils.SpringContextUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Slf4j
public final class EmailUtil {

    private final SpringTemplateEngine templateEngine;

    private final IEmailSenderService emailSenderService;


    private final IEmailTemplateFeignClient emailTemplateFeignClient;

    private final RedisUtil redisUtil;

    private final String DEFAULT_SENDER = "DEFAULT_SENDER";

    @Lazy
    private EmailUtil() {
        this.templateEngine = SpringContextUtil.getBean(SpringTemplateEngine.class);
        this.emailSenderService = SpringContextUtil.getBean(IEmailSenderService.class);
        this.emailTemplateFeignClient = SpringContextUtil.getBean(IEmailTemplateFeignClient.class);
        this.redisUtil = SpringContextUtil.getBean(RedisUtil.class);
        try {
            List<EmailSenderConfig> emailSenderConfigList = emailTemplateFeignClient.getAllSenderConfig();
            for (EmailSenderConfig emailSenderConfig : emailSenderConfigList) {
                this.refreshJavaMailSender(emailSenderConfig.getUserName());
            }
        } catch (Exception e) {
            log.error("从数据库中初始化邮件发送者失败{}", e.getMessage());
        }
    }

    private final static EmailUtil emailUtil = new EmailUtil();

    public static EmailUtil single() {
        return emailUtil;
    }


    public void refreshJavaMailSender(String userName) {
        EmailSenderConfig emailSenderConfig = emailTemplateFeignClient.getSenderConfigByUserName(userName);
        if (emailSenderConfig.getStatus()) {
            try {
                log.info("重新刷新{}的配置", userName);
                redisUtil.hashSet(MAIL_SENDER_MAP_KEY, userName, emailSenderConfig);
            } catch (Exception e) {
                log.error("{}配置信息错误{}", userName, e.getMessage());
                redisUtil.hashDel(userName);
            }

        } else {
            redisUtil.hashDel(userName);
        }
    }

    @NotNull
    private JavaMailSenderImpl getJavaMailSender(EmailSenderConfig emailSenderConfig) throws Exception {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderSerializable();
        javaMailSender.setUsername(emailSenderConfig.getUserName());
        javaMailSender.setPassword(AES128Util.single().decrypt(emailSenderConfig.getPassword()));
        javaMailSender.setProtocol(emailSenderConfig.getProtocol());
        javaMailSender.setHost(emailSenderConfig.getHost());
        javaMailSender.setPort(emailSenderConfig.getPort());
        javaMailSender.getJavaMailProperties().put("mail.smtp.auth", true);
        javaMailSender.getJavaMailProperties().put("mail.smtp.socketFactory.fallback", false);
        if (emailSenderConfig.getTlsEnabled()) {
            javaMailSender.getJavaMailProperties().put("mail.smtp.ssl.enable", true);
            javaMailSender.getJavaMailProperties().put("mail.smtp.socketFactory.port", emailSenderConfig.getPort());
            javaMailSender.getJavaMailProperties().put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }
        if (emailSenderConfig.getTlsEnabled()) {
            javaMailSender.getJavaMailProperties().put("mail.smtp.starttls.enable", true);
            javaMailSender.getJavaMailProperties().put("mail.smtp.socketFactory.port", emailSenderConfig.getPort());
            javaMailSender.getJavaMailProperties().put("mail.smtp.socketFactory.class", "com.sun.mail.util.MailSSLSocketFactory");
        }
        return javaMailSender;
    }

    public IEmailSenderService getSendService(final String userName) {
        try {
            EmailSenderConfig emailSenderConfig = redisUtil.hashGet(MAIL_SENDER_MAP_KEY, userName);
            if (emailSenderConfig == null) {
                return this.emailSenderService;
            }
            return new EmailSenderServiceImpl(this.getJavaMailSender(emailSenderConfig), this.templateEngine, userName);
        } catch (Exception e) {
            return this.emailSenderService;
        }
    }

    public Future<String> sendEmail(MailVO mailVO) throws Exception {
        return sendEmail(DEFAULT_SENDER, mailVO);
    }

    public Future<String> sendEmail(String userName, MailVO mailVO) throws Exception {
        IEmailSenderService senderService = getSendService(userName);
        return senderService.sendEmail(mailVO);
    }

    public Future<String> sendEmail(String templateCode, EmailParams params, String language) throws Exception {
        EmailTemplate emailTemplate = emailTemplateFeignClient.getEmailTemplateByCode(templateCode, language);
        if (emailTemplate == null) {
            throw new BusinessException(String.format("邮件模板【%s】未找到！", templateCode));
        }
        IEmailSenderService senderService = getSendService(emailTemplate.getFromAddress());
        return senderService.sendEmail(templateCode, params, language);
    }

    public Future<String> sendEmail(String templateCode, EmailParams params) throws Exception {
        return sendEmail(templateCode, params, "zh_CN");
    }
}
