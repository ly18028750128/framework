package org.cloud.filter;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InnerFeignConfiguration {

    @Value("#{'${system.api.inner.url-patterns:/inner/*}'.split(',')}")
    List<String> innerApis;

    @Bean
    public FilterRegistrationBean<InnerApiFilter> registerFilter() {
        FilterRegistrationBean<InnerApiFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new InnerApiFilter());
        registration.addUrlPatterns(innerApis.toArray(new String[]{}));
        registration.setName("InnerApiFilter");
        return registration;
    }

}
