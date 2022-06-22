package com.unknow.first.mail.manager.service.impl;

import com.unknow.first.mail.manager.service.IEmailSenderService;
import com.unknow.first.mail.manager.vo.MailVO;
import java.io.File;
import java.util.Date;
import javax.mail.internet.MimeMessage;
import org.cloud.utils.CollectionUtil;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

public class EmailSenderServiceImpl implements IEmailSenderService {

    private JavaMailSender javaMailSender;

    private TemplateEngine templateEngine;

    public EmailSenderServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendEmail(MailVO mailVO) throws Exception {
        if (CollectionUtil.single().isEmpty(mailVO.getTemplateText())) {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            this.setMessageBaseInfo(simpleMailMessage, mailVO);
            simpleMailMessage.setText(mailVO.getText());
            javaMailSender.send(simpleMailMessage);
        } else {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            Context ctx = new Context();
            ctx.setVariables(mailVO.getEmailParams());
            String emailText = templateEngine.process(mailVO.getTemplateText(), ctx);
            mimeMessage.setText(emailText);
            if (CollectionUtil.single().isNotEmpty(mailVO.getFiles())) {
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                for (File file : mailVO.getFiles()) {
                    helper.addAttachment(file.getName(), file);
                }
            }
            javaMailSender.send(mimeMessage);
        }
    }

    @Override
    public void sendEmail(MailVO mailVO, Integer templateId) throws Exception {
        // todo 通过数据库获取templateString
        this.sendEmail(mailVO);
    }


    private void setMessageBaseInfo(MailMessage message, MailVO mailVO) throws Exception {
        message.setFrom(mailVO.getFrom());
        message.setTo(mailVO.getTo());
        message.setCc(mailVO.getCc());
        message.setBcc(mailVO.getBcc());
        message.setSubject(mailVO.getSubject());
        message.setSentDate(new Date());
    }

}
