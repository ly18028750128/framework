package org.cloud.context;

import org.springframework.beans.BeanUtils;

public class RequestContextManager {

    private static ThreadLocal<RequestContext> requestContextThreadLocal = new ThreadLocal<>();

    private RequestContextManager() {
    }

    private static class HandlerClass {
        private HandlerClass() {
        }

        private static RequestContextManager instance = new RequestContextManager();
    }

    public static RequestContextManager single() {
        return HandlerClass.instance;
    }

    public void setRequestContext(RequestContext requestContext) {
        requestContextThreadLocal.set(requestContext);
    }

    public RequestContext getRequestContext() {
        return requestContextThreadLocal.get();
    }

    // 克隆一个出来，可以避免多线程时的一些问题，多线程时请尽量将变量放在properties里。
    public RequestContext cloneRequestContext() {
        RequestContext requestContext = new RequestContext();
        BeanUtils.copyProperties(this.getRequestContext(), requestContext);
        requestContext.getProperties().putAll(this.getRequestContext().getProperties());
        return requestContext;
    }

    //todo 需要开发
    public RequestContext createRequestContext(String userName) {
        RequestContext requestContext = new RequestContext();

        return requestContext;
    }

}
