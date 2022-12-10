package com.unknow.first.mail.manager.util;


import com.unknow.first.mail.manager.domain.EmailSenderConfig;
import com.unknow.first.mail.manager.domain.EmailTemplate;
import com.unknow.first.mail.manager.feign.IEmailTemplateFeignClient;
import com.unknow.first.mail.manager.service.IEmailSenderService;
import com.unknow.first.mail.manager.service.impl.EmailSenderServiceImpl;
import com.unknow.first.mail.manager.vo.MailVO;
import com.unknow.first.mail.manager.vo.MailVO.EmailParams;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.cloud.exception.BusinessException;
import org.cloud.utils.AES128Util;
import org.cloud.utils.SpringContextUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.util.StringUtils;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Slf4j
public final class EmailUtil {

    private final SpringTemplateEngine templateEngine;

    private final IEmailSenderService emailSenderService;

    private Map<String, IEmailSenderService> javaMailSenderMap = new LinkedHashMap<>();

    private final IEmailTemplateFeignClient emailTemplateFeignClient;

    private final String DEFAULT_SENDER = "DEFAULT_SENDER";

    @Lazy
    private EmailUtil() {
        this.templateEngine = SpringContextUtil.getBean(SpringTemplateEngine.class);
        this.emailSenderService = SpringContextUtil.getBean(IEmailSenderService.class);
        this.emailTemplateFeignClient = SpringContextUtil.getBean(IEmailTemplateFeignClient.class);
        javaMailSenderMap.put(DEFAULT_SENDER, this.emailSenderService);
        List<EmailSenderConfig> emailSenderConfigList = emailTemplateFeignClient.getAllSenderConfig();
        for (EmailSenderConfig emailSenderConfig : emailSenderConfigList) {
            this.refreshJavaMailSender(emailSenderConfig.getUserName());
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
                JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
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
                javaMailSenderMap.put(userName, new EmailSenderServiceImpl(javaMailSender, templateEngine));
            } catch (Exception e) {
                log.error("{}配置信息错误{}", userName, e.getMessage());
                javaMailSenderMap.remove(userName);
            }

        } else {
            javaMailSenderMap.remove(userName);
        }
    }

    public IEmailSenderService get(final String userName) {
        return javaMailSenderMap.get(userName);
    }

    public void sendEmail(MailVO mailVO) throws Exception {
        sendEmail(DEFAULT_SENDER, mailVO);
    }

    public void sendEmail(String userName, MailVO mailVO) throws Exception {
        IEmailSenderService senderService = javaMailSenderMap.get(userName);
        if (senderService == null) {
            senderService = javaMailSenderMap.get(DEFAULT_SENDER);
        }
        senderService.sendEmail(mailVO);
    }

    public void sendEmail(String templateCode, EmailParams params, String language) throws Exception {
        EmailTemplate emailTemplate = emailTemplateFeignClient.getEmailTemplateByCode(templateCode, language);
        if (emailTemplate == null) {
            throw new BusinessException(String.format("邮件模板【%s】未找到！", templateCode));
        }
        IEmailSenderService senderService;
        if (StringUtils.hasLength(emailTemplate.getFromAddress())) {
            senderService = javaMailSenderMap.get(emailTemplate.getFromAddress());
            if (senderService == null) {
                senderService = javaMailSenderMap.get(DEFAULT_SENDER);
            }
        } else {
            senderService = javaMailSenderMap.get(DEFAULT_SENDER);
        }
        senderService.sendEmail(templateCode, params, language);
    }

    public void sendEmail(String templateCode, EmailParams params) throws Exception {
        sendEmail(templateCode, params, "zh_CN");
    }
}
