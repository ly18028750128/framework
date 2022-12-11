package com.unknow.first.mail.manager.service.impl;

import com.unknow.first.mail.manager.domain.EmailTemplate;
import com.unknow.first.mail.manager.feign.IEmailTemplateFeignClient;
import com.unknow.first.mail.manager.service.IEmailSenderService;
import com.unknow.first.mail.manager.vo.MailVO;
import com.unknow.first.mail.manager.vo.MailVO.EmailParams;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import lombok.extern.slf4j.Slf4j;
import org.cloud.utils.CollectionUtil;
import org.cloud.utils.CommonUtil;
import org.cloud.utils.SpringContextUtil;
import org.cloud.utils.process.ProcessUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Slf4j
public class EmailSenderServiceImpl implements IEmailSenderService {

    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine templateEngine;

    private final IEmailTemplateFeignClient emailTemplateFeignClient;

    @Lazy
    public EmailSenderServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.emailTemplateFeignClient = SpringContextUtil.getBean(IEmailTemplateFeignClient.class);
    }

    public EmailSenderServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine, IEmailTemplateFeignClient emailTemplateFeignClient) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.emailTemplateFeignClient = emailTemplateFeignClient;
    }

    @Override
    public Future<String> sendEmail(MailVO mailVO) throws Exception {

        return ProcessUtil.single().runCallable(() -> {
            try {
                if (CollectionUtil.single().isEmpty(mailVO.getTemplateText())) {
                    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                    setMessageBaseInfo(simpleMailMessage, mailVO);
                    simpleMailMessage.setText(mailVO.getText());
                    javaMailSender.send(simpleMailMessage);
                } else {
                    MimeMessage mimeMessage = getMimeMessage(mailVO);
                    Context ctx = new Context();
                    ctx.setVariables(mailVO.getParams().getEmailParams());
                    String emailText = templateEngine.process(mailVO.getTemplateText(), ctx);
                    mimeMessage.setContent(emailText, "text/html;charset=GBK");
                    if (CollectionUtil.single().isNotEmpty(mailVO.getFiles())) {
                        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                        for (File file : mailVO.getFiles()) {
                            helper.addAttachment(file.getName(), file);
                        }
                    }
                    javaMailSender.send(mimeMessage);
                }
                return "邮件发送成功！";
            } catch (Exception e) {
                return String.format("邮件发送失败！%s", e.getMessage());
            }
        });
    }

    @NotNull
    private MimeMessage getMimeMessage(MailVO mailVO) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.setFrom(CommonUtil.single().getEnv("spring.mail.username", ""));

        mimeMessage.addRecipients(RecipientType.TO, convertToAddressList(mailVO.getTo()));
        if (CollectionUtil.single().isNotEmpty((mailVO.getBcc()))) {
            mimeMessage.addRecipients(RecipientType.BCC, convertToAddressList(mailVO.getBcc()));
        }
        if (CollectionUtil.single().isNotEmpty((mailVO.getCc()))) {
            mimeMessage.addRecipients(RecipientType.CC, convertToAddressList(mailVO.getCc()));
        }

        mimeMessage.setSentDate(new Date());
        if (CollectionUtil.single().isNotEmpty(mailVO.getParams().getSubjectParams())) {
            String subject = getSubject(mailVO);
            mimeMessage.setSubject(subject);
        } else {
            mimeMessage.setSubject(mailVO.getSubject());
        }
        return mimeMessage;
    }

    private Address[] convertToAddressList(String[] address) {
        return Arrays.stream(address).map(s -> {
            try {
                return new InternetAddress(s);
            } catch (AddressException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList()).toArray(new Address[]{});
    }


    @Override
    public Future<String> sendEmail(String templateCode, @NotNull EmailParams params) throws Exception {
        return this.sendEmail(templateCode, params, "zh_CN");
    }

    @Override
    public Future<String> sendEmail(String templateCode, @NotNull EmailParams params, String language) throws Exception {
        MailVO mailVO = new MailVO();
        EmailTemplate emailTemplate = emailTemplateFeignClient.getEmailTemplateByCode(templateCode, language);
        mailVO.setTo(emailTemplate.getToAddress().split(","));
        if (emailTemplate.getBcc() != null) {
            mailVO.setBcc(emailTemplate.getBcc().split(","));
        }
        if (emailTemplate.getCc() != null) {
            mailVO.setCc(emailTemplate.getCc().split(","));
        }
        mailVO.setTemplateText(emailTemplate.getTemplateText());
        mailVO.setParams(params);
        mailVO.setSubject(emailTemplate.getSubject());
        return this.sendEmail(mailVO);
    }

    private void setMessageBaseInfo(MailMessage message, MailVO mailVO) throws Exception {
        message.setFrom(CommonUtil.single().getEnv("spring.mail.username", ""));
        message.setTo(mailVO.getTo());
        message.setCc(mailVO.getCc());
        message.setBcc(mailVO.getBcc());

        if (CollectionUtil.single().isNotEmpty(mailVO.getParams().getSubjectParams())) {
            String subject = getSubject(mailVO);
            message.setSubject(subject);
        } else {
            message.setSubject(mailVO.getSubject());
        }
        message.setSentDate(new Date());
    }

    private String getSubject(MailVO mailVO) {
        String subject = mailVO.getSubject();
        for (Entry<String, String> entry : mailVO.getParams().getSubjectParams().entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            subject = subject.replace("{{" + key + "}}", value);
        }
        return subject;
    }

}
