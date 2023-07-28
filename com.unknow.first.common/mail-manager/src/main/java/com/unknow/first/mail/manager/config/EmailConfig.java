package com.unknow.first.mail.manager.config;


import brave.Tracer;
import com.unknow.first.mail.manager.feign.IEmailTemplateFeignClient;
import com.unknow.first.mail.manager.service.IEmailSenderService;
import com.unknow.first.mail.manager.service.impl.EmailSenderServiceImpl;
import org.cloud.feign.service.ICommonServiceMessageLogFeign;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.StringTemplateResolver;

@Configuration
//@ConditionalOnProperty(prefix = "system.email", name = "enable", matchIfMissing = true)
@ComponentScan({"com.unknow.first.mail.manager"})
@MapperScan({"com.unknow.first.mail.manager.mapper"})
@EnableFeignClients(basePackages = {"com.unknow.first.mail.manager.feign"})
public class EmailConfig {


    @Bean
    public StringTemplateResolver stringTemplateResolver() {
        StringTemplateResolver stringTemplateResolver = new StringTemplateResolver();
        stringTemplateResolver.setCacheable(true);
        stringTemplateResolver.setTemplateMode(TemplateMode.HTML);
        return stringTemplateResolver;
    }

    @Bean
    @Primary
    public SpringTemplateEngine springTemplateEngine(StringTemplateResolver stringTemplateResolver) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(stringTemplateResolver);
        return templateEngine;
    }

    @Bean
    public IEmailSenderService emailSenderService(JavaMailSender javaMailSender, SpringTemplateEngine springTemplateEngine,
        IEmailTemplateFeignClient emailTemplateFeignClient, ICommonServiceMessageLogFeign serviceMessageLogFeign, Tracer tracer) {
        return new EmailSenderServiceImpl(javaMailSender, springTemplateEngine, emailTemplateFeignClient, serviceMessageLogFeign, tracer);
    }

}
