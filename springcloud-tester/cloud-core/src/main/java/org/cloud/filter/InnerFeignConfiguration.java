package org.cloud.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InnerFeignConfiguration {

    @Value("#{'${system.api.inner.url-patterns:/inner/*}'.split(',')}")
    String[] innerApis;


    @Bean
    public FilterRegistrationBean registerFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new InnerApiFilter());
        registration.addUrlPatterns(innerApis);
        registration.setName("InnerApiFilter");
        return registration;
    }

}
