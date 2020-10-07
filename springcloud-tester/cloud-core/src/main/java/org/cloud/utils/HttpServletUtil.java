package org.cloud.utils;

import com.alibaba.fastjson.JSON;
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
        if (excludedAuthPages != null && excludedAuthPages.size() != 0) {
            for (String exclude : excludedAuthPages) {
                final PathMatcher pathMathcer = new AntPathMatcher();
                if (pathMathcer.match(exclude, httpServletRequest.getRequestURI())) {
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
        log.error(CommonUtil.single().getStackTrace(e));
        httpServletResponse.setContentType("application/json;charset=utf8");
        httpServletResponse.getWriter().write(JSON.toJSONString(responseResult));
    }
}
