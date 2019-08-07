package org.cloud.utils;

import org.cloud.utils.process.ProcessCallable;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class HttpServletUtil {

    private HttpServletUtil() {
    }

    private static class Handler {
        private void Handler() {
        }

        private static HttpServletUtil handler = new HttpServletUtil();
    }

    public static HttpServletUtil signle() {
        return Handler.handler;
    }

    public HttpServletRequest getHttpServlet() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (servletRequestAttributes != null) {
            request = servletRequestAttributes.getRequest();
        }

        if (request == null) {
            request = ProcessCallable.getHttpServletRequest();
        }

        return request;
    }

    public HttpServletResponse getHttpServletResponse() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = null;
        if (servletRequestAttributes != null) {
            response = servletRequestAttributes.getResponse();
        }

        if (response != null) {
            response = ProcessCallable.getHttpServletResponse();
        }

        return response;
    }


}
