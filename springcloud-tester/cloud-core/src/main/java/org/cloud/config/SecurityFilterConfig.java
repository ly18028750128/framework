package org.cloud.config;

import lombok.Setter;
import org.cloud.filter.SecurityFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "spring.security")
@Setter
public class SecurityFilterConfig {
    private List<String> excludedAuthPages;

    @Bean
    public FilterRegistrationBean securityFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new SecurityFilter(excludedAuthPages));
        registration.addUrlPatterns("/*"); //
        registration.setName("securityFilter");
        registration.setOrder(0);
        return registration;
    }
}
