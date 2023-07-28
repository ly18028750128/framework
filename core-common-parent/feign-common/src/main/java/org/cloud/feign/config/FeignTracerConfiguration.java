package org.cloud.feign.config;

import brave.Tracer;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignTracerConfiguration {
    public final static String FEIGN_REQUEST_TRACER_ID = "feign_request_tracer_id";

    @Bean
    public RequestInterceptor feignTracerInterceptor(Tracer tracer) {
        return requestTemplate -> requestTemplate.header(FEIGN_REQUEST_TRACER_ID, tracer.currentSpan().context().traceIdString());
    }
}
