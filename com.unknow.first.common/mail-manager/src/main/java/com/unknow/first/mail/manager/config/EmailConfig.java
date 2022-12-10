package com.unknow.first.mail.manager.config;


import com.unknow.first.mail.manager.service.IEmailSenderService;
import com.unknow.first.mail.manager.service.impl.EmailSenderServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@Configuration
@ConditionalOnProperty(prefix = "system.email", name = "enable",matchIfMissing = true)
@ComponentScan({"com.unknow.first.mail.manager.*"})
@MapperScan({"com.unknow.first.mail.manager.mapper"})
//@EnableFeignClients(basePackages = {"com.unknow.first.mail.manager.feign"})
public class EmailConfig {



    @Bean
    public StringTemplateResolver stringTemplateResolver() {
        StringTemplateResolver stringTemplateResolver = new StringTemplateResolver();
        stringTemplateResolver.setCacheable(true);
        stringTemplateResolver.setTemplateMode(TemplateMode.HTML);
        return stringTemplateResolver;
    }

    @Bean
    public SpringTemplateEngine springTemplateEngine(StringTemplateResolver stringTemplateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(stringTemplateResolver);
        return templateEngine;
    }

    @Bean
    public IEmailSenderService emailSenderService(JavaMailSender javaMailSender, SpringTemplateEngine springTemplateEngine) {
        return new EmailSenderServiceImpl(javaMailSender, springTemplateEngine);
    }

}
