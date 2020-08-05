package org.cloud.filter;

import brave.Tracer;
import lombok.extern.slf4j.Slf4j;
import org.cloud.feign.FeignTracerConfiguration;
import org.cloud.utils.SpringContextUtil;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 内部调用过滤器，通过system.api.inner.url-patterns进行配置，默认为/inner/*
 */
@Slf4j
public class InnerApiFilter extends OncePerRequestFilter {
    private Tracer tracer;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (tracer == null) {
            tracer = SpringContextUtil.getBean(Tracer.class);
        }
        log.info("tracer.currentSpan().context().traceIdString().id={}", tracer.currentSpan().context().traceIdString());
        log.info("request.getHeader(FeignTracerConfiguration.FEIGN_REQUEST_TRACER_ID)={}", httpServletRequest.getHeader(FeignTracerConfiguration.FEIGN_REQUEST_TRACER_ID));
        if (!tracer.currentSpan().context().traceIdString().equals(httpServletRequest.getHeader(FeignTracerConfiguration.FEIGN_REQUEST_TRACER_ID))) {
            throw new ServletException("非Feign内部调用，拒绝请求");
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
