package org.cloud.feign.filter;


import static org.cloud.feign.config.FeignTracerConfiguration.FEIGN_REQUEST_TRACER_ID;

import brave.Tracer;
import com.alibaba.fastjson.JSON;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.cloud.vo.CommonApiResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 内部调用过滤器，通过system.api.inner.url-patterns进行配置，默认为/inner/*
 */
@Slf4j
public class InnerApiFilter extends OncePerRequestFilter {

    Tracer tracer;

    public InnerApiFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
        throws ServletException, IOException {
        if (!tracer.currentSpan().context().traceIdString().equals(httpServletRequest.getHeader(FEIGN_REQUEST_TRACER_ID))) {
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            httpServletResponse.getOutputStream().write(JSON.toJSONString(CommonApiResult.createFailResult("非Feign内部调用，拒绝请求")).getBytes());
            httpServletResponse.getOutputStream().flush();
            return;
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
