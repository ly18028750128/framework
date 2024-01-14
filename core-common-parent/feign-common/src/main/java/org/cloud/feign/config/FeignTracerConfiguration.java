package org.cloud.feign.config;

import brave.Tracer;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FeignTracerConfiguration {
    public final static String FEIGN_REQUEST_TRACER_ID = "feign_request_tracer_id";

    @Bean
    public RequestInterceptor feignTracerInterceptor(Tracer tracer) {
        return requestTemplate -> {
            try{
                requestTemplate.header(FEIGN_REQUEST_TRACER_ID, tracer.currentSpan().context().traceIdString());
            }catch (Exception e){
                log.warn("设置feign跟踪路由信息失败",e);
            }

        };
    }
}
