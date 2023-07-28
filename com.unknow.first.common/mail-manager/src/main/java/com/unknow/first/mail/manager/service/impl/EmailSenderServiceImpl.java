package com.unknow.first.mail.manager.service.impl;

import brave.ScopedSpan;
import brave.Tracer;
import com.unknow.first.mail.manager.domain.EmailTemplate;
import com.unknow.first.mail.manager.feign.IEmailTemplateFeignClient;
import com.unknow.first.mail.manager.service.IEmailSenderService;
import com.unknow.first.mail.manager.vo.MailVO;
import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import lombok.extern.slf4j.Slf4j;
import org.cloud.core.redis.RedisUtil;
import org.cloud.feign.service.ICommonServiceMessageLogFeign;
import org.cloud.utils.CollectionUtil;

import org.cloud.utils.EnvUtil;
import org.cloud.utils.SpringContextUtil;
import org.cloud.utils.process.ProcessUtil;
import org.cloud.vo.MessageLogVO;
import org.cloud.vo.MessageLogVO.MessageLogVOBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Slf4j
public class EmailSenderServiceImpl implements IEmailSenderService {

    private static final long serialVersionUID = 1412048124719274197L;

    private final JavaMailSender javaMailSender;

    private final SpringTemplateEngine templateEngine;

    private final IEmailTemplateFeignClient emailTemplateFeignClient;

    private final ICommonServiceMessageLogFeign serviceMessageLogFeign;

    private final String userName;

    private final Tracer tracer;

    @Lazy
    public EmailSenderServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine, String userName) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.emailTemplateFeignClient = SpringContextUtil.getBean(IEmailTemplateFeignClient.class);
        this.serviceMessageLogFeign = SpringContextUtil.getBean(ICommonServiceMessageLogFeign.class);
        this.tracer = SpringContextUtil.getBean(Tracer.class);
        this.userName = userName;
    }

    @Lazy
    public EmailSenderServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine, IEmailTemplateFeignClient emailTemplateFeignClient,
        ICommonServiceMessageLogFeign serviceMessageLogFeign, Tracer tracer) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.emailTemplateFeignClient = emailTemplateFeignClient;
        this.serviceMessageLogFeign = serviceMessageLogFeign;
        this.tracer = tracer;
        if (this.javaMailSender instanceof JavaMailSenderImpl) {
            this.userName = Objects.requireNonNull(((JavaMailSenderImpl) this.javaMailSender).getUsername());
        } else {
            this.userName = EnvUtil.single().getEnv("spring.mail.username", "");
        }
    }

    @Override
    public Future<String> sendEmail(final MailVO mailVO) throws Exception {
        return ProcessUtil.single().runCallable(() -> sentEmailFinal(mailVO));
    }

    private String sentEmailFinal(final MailVO mailVO) {
        final String currentServiceName = EnvUtil.single().getEnv("spring.application.name", "").toLowerCase();
        MessageLogVOBuilder logBuilder = MessageLogVO.builder();
        logBuilder.serviceName(StringUtils.hasLength(mailVO.getServiceName()) ? mailVO.getServiceName() : currentServiceName);
        logBuilder.sender(this.userName);
        logBuilder.to(String.join(",", mailVO.getTo()));
        logBuilder.bcc(String.join(",", mailVO.getBcc()));
        logBuilder.cc(String.join(",", mailVO.getCc()));
        logBuilder.type("EMAIL");
        logBuilder.sendDate(new Date());
        String resultMsg = "邮件发送成功！";
        try {
            if (CollectionUtil.single().isEmpty(mailVO.getTemplateText())) {
                SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                setMessageBaseInfo(simpleMailMessage, mailVO);
                simpleMailMessage.setText(mailVO.getText());
                logBuilder.subject(simpleMailMessage.getSubject());
                logBuilder.content(mailVO.getText());
                javaMailSender.send(simpleMailMessage);
            } else {
                MimeMessage mimeMessage = getMimeMessage(mailVO);
                Context ctx = new Context();
                ctx.setVariables(mailVO.getParams().getEmailParams());
                String emailText = templateEngine.process(mailVO.getTemplateText(), ctx);
                mimeMessage.setContent(emailText, "text/html;charset=GBK");
                logBuilder.templateCode(mailVO.getTemplateCode());
                logBuilder.subject(mimeMessage.getSubject());
                logBuilder.content(emailText);
                if (CollectionUtil.single().isNotEmpty(mailVO.getFiles())) {
                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                    for (File file : mailVO.getFiles()) {
                        helper.addAttachment(file.getName(), file);
                    }
                }
                javaMailSender.send(mimeMessage);
            }
            logBuilder.result("success");
        } catch (Exception e) {
            logBuilder.result("fail");
            resultMsg = String.format("邮件发送失败！%s", e.getMessage());
            logBuilder.content(resultMsg);
            if (mailVO.getRetryCount() < 3) {
                mailVO.increaseRetryCount();
                mailVO.setFrom(userName);
                mailVO.setServiceName(currentServiceName);
                RedisUtil.single().listLeftPush(RETRY_QUEUE_KEY, mailVO);
            }
        }
        ScopedSpan span = tracer.startScopedSpan(UUID.randomUUID().toString());
        try {
            Boolean result = serviceMessageLogFeign.saveMessageLogs(logBuilder.build());
        } catch (Exception e) {
            log.error("邮件日志保存失败{}", e.getMessage());
        } finally {
            span.finish(); // clean up after yourself
        }
        return resultMsg;
    }

    @Override
    public Future<String> sendEmail(String templateCode, MailVO mailVO, String language) throws Exception {
        EmailTemplate emailTemplate = emailTemplateFeignClient.getEmailTemplateByCode(templateCode, language);

        if (mailVO == null) {
            mailVO = new MailVO();
        }

        if (CollectionUtil.single().isEmpty(mailVO.getTo())) {
            mailVO.getTo().addAll(Arrays.asList(emailTemplate.getToAddress().split(",")));
        }
        if (StringUtils.hasLength(emailTemplate.getBcc()) && CollectionUtil.single().isEmpty(mailVO.getBcc())) {
            mailVO.getBcc().addAll(Arrays.asList(emailTemplate.getBcc().split(",")));
        }

        if (StringUtils.hasLength(emailTemplate.getCc()) && CollectionUtil.single().isEmpty(mailVO.getCc())) {
            mailVO.getCc().addAll(Arrays.asList(emailTemplate.getCc().split(",")));
        }

        mailVO.setTemplateText(emailTemplate.getTemplateText());
        mailVO.setTemplateCode(templateCode);
        mailVO.setSubject(emailTemplate.getSubject());
        return this.sendEmail(mailVO);
    }

    private void setMessageBaseInfo(MailMessage message, MailVO mailVO) throws Exception {
        message.setFrom(this.userName);
        message.setTo(mailVO.getTo().toArray(new String[]{}));
        message.setCc(mailVO.getCc().toArray(new String[]{}));
        message.setBcc(mailVO.getBcc().toArray(new String[]{}));
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

    @NotNull
    private MimeMessage getMimeMessage(MailVO mailVO) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.setFrom(this.userName);
        mimeMessage.addRecipients(RecipientType.TO, convertToAddressList(mailVO.getTo().toArray(new String[]{})));
        if (CollectionUtil.single().isNotEmpty((mailVO.getBcc()))) {
            mimeMessage.addRecipients(RecipientType.BCC, convertToAddressList(mailVO.getBcc().toArray(new String[]{})));
        }
        if (CollectionUtil.single().isNotEmpty((mailVO.getCc()))) {
            mimeMessage.addRecipients(RecipientType.CC, convertToAddressList(mailVO.getCc().toArray(new String[]{})));
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

}
