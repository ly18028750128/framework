package org.cloud.feign.config;

import brave.Tracer;
import feign.RequestInterceptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.cloud.feign.filter.InnerApiFilter;
import org.cloud.utils.CollectionUtil;
import org.cloud.utils.HttpServletUtil;
import org.cloud.utils.RestTemplateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;

@Configuration
@EnableFeignClients(basePackages = {"org.cloud.feign.service"})
public class CommonFeignConfig {

    @Value("#{'${system.api.inner.url-patterns:/inner/*}'.split(',')}")
    private List<String> innerApis;

    @Bean
    @Order(9)
    public FilterRegistrationBean<InnerApiFilter> registerFilter(Tracer tracer) {
        FilterRegistrationBean<InnerApiFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new InnerApiFilter(tracer));
        if(CollectionUtil.single().isEmpty(innerApis)){
            innerApis = new ArrayList<>();
        }
        innerApis.add("/inner/*");
        registration.addUrlPatterns(innerApis.toArray(new String[]{}));
        registration.setName("InnerApiFilter");
        return registration;
    }

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            HttpServletRequest request = HttpServletUtil.single().getHttpServlet();
            if (request == null) {
                return;
            }
            HttpHeaders headers = RestTemplateUtil.single().getHttpHeadersFromHttpRequest(request, new String[]{"authorization", "cookie"});
            if (headers == null || headers.isEmpty()) {
                return;
            }
            Map<String, Collection<String>> headersMap = new HashMap<>(headers);
            requestTemplate.headers(headersMap);

        };
    }

}
