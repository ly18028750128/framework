package com.unknow.first.mail.manager.config;


import com.unknow.first.mail.manager.service.IEmailSenderService;
import com.unknow.first.mail.manager.service.impl.EmailSenderServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;

@Configuration
@ConditionalOnProperty(prefix = "system.email", name = "enable", havingValue = "true")
public class EmailConfig {

    @Bean
    public IEmailSenderService emailSenderService(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        return new EmailSenderServiceImpl(javaMailSender, templateEngine);
    }
}
