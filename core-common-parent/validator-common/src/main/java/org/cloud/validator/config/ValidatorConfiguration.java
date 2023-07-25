package org.cloud.validator.config;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.cloud.validator.utils.ValidateUtil;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatorConfiguration {

    @Bean
    public Validator validator() {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
            .configure()
            .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        ValidateUtil.single().setValidator(validator);
        return validator;
    }
}