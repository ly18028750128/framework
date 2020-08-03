package org.cloud.feign;

import brave.Tracer;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 用于添加feign_request_tracer_id请求头到请求头中，用于识别是否为内部调用，参考示例为UserInfoController类
 *
 * @author longyou
 * @create 2020/8/3
 * @since 1.0.0
 */
@Configuration
@Slf4j
public class FeignTracerConfiguration {

    public final static String FEIGN_REQUEST_TRACER_ID = "feign_request_tracer_id";

    @Autowired
    Tracer tracer;

    @Bean
    public RequestInterceptor feignTracerInterceptor() {
        return requestTemplate -> {
            requestTemplate.header(FEIGN_REQUEST_TRACER_ID, tracer.currentSpan().context().traceIdString());
        };
    }
}
