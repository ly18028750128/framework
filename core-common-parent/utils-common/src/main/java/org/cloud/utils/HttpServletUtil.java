package org.cloud.utils;

import com.alibaba.fastjson.JSON;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.cloud.context.RequestContextManager;
import org.cloud.exception.BusinessException;
import org.cloud.vo.ResponseResult;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
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

        if (request == null && RequestContextManager.single().getRequestContext() != null) {
            request = RequestContextManager.single().getRequestContext().getHttpServletRequest();
        }
        return request;
    }

    public HttpServletResponse getHttpServletResponse() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = null;
        if (servletRequestAttributes != null) {
            response = servletRequestAttributes.getResponse();
        }

        if (response != null && RequestContextManager.single().getRequestContext() != null) {
            response = RequestContextManager.single().getRequestContext().getHttpServletResponse();
        }

        return response;
    }

    public boolean isExcludeUri(HttpServletRequest httpServletRequest, final List<String> excludedAuthPages) {
        boolean isExcludeUri = false;
        final String uri = httpServletRequest.getRequestURI();
        if (excludedAuthPages != null && excludedAuthPages.size() != 0) {
            for (String exclude : excludedAuthPages) {
                final PathMatcher pathMathcer = new AntPathMatcher();
                if (pathMathcer.match(exclude, uri)) {
                    isExcludeUri = true;
                    break;
                }
            }
        }
        return isExcludeUri;
    }

    public boolean isExcludeUri(HttpServletRequest httpServletRequest, final String excludedAuthPages) {
        return isExcludeUri(httpServletRequest, Stream.of(excludedAuthPages.split(",")).collect(Collectors.toList()));
    }

    public boolean isExcludeUri(final List<String> excludedAuthPages) {
        return isExcludeUri(this.getHttpServlet(), excludedAuthPages);
    }

    public boolean isExcludeUri(final String excludedAuthPages) {
        return isExcludeUri(this.getHttpServlet(), Stream.of(excludedAuthPages.split(",")).collect(Collectors.toList()));
    }

    public void handlerBusinessException(Exception e, HttpServletResponse httpServletResponse) throws Exception {
        BusinessException businessException;
        if (e instanceof BusinessException) {
            businessException = (BusinessException) e;
        } else if (e.getCause() instanceof BusinessException) {
            businessException = (BusinessException) e.getCause();
        } else {
            businessException = new BusinessException(e.getMessage(), e, HttpStatus.BAD_REQUEST.value());
        }
        ResponseResult responseResult = ResponseResult.createFailResult();
        responseResult.setMessage(businessException.getMessage());
        responseResult.setData(businessException.getErrObject());
        httpServletResponse.setStatus(businessException.getHttpStatus());
        log.error(ExceptionUtil.single().getStackTrace(e));
        httpServletResponse.setContentType("application/json;charset=utf8");
        httpServletResponse.getWriter().write(JSON.toJSONString(responseResult));
    }

    public void setCacheTime(HttpServletResponse response) {
        // 设置缓存过期时间为1天（单位为秒）
        long cacheExpiration = TimeUnit.DAYS.toSeconds(1) * 365;
        response.setHeader("Cache-Control", "public, max-age=" + cacheExpiration);
        // 设置最后修改时间为当前时间
        long currentTime = System.currentTimeMillis();
        response.setDateHeader("Last-Modified", currentTime);
        // 设置响应的过期时间为1天后
        long expirationTime = currentTime + cacheExpiration * 1000;
        response.setDateHeader("Expires", expirationTime);
    }
}
