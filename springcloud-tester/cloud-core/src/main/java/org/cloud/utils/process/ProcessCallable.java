package org.cloud.utils.process;

import org.cloud.utils.HttpServletUtil;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.Callable;

public abstract class ProcessCallable<V> implements Callable, Runnable {

    private static final ThreadLocal<HttpServletRequest> requestThreadLocal = new ThreadLocal<HttpServletRequest>();
    private static final ThreadLocal<HttpServletResponse> responseThreadLocal = new ThreadLocal<HttpServletResponse>();

    public static HttpServletRequest getHttpServletRequest() {
        return requestThreadLocal.get();
    }

    public static HttpServletResponse getHttpServletResponse() {
        return responseThreadLocal.get();
    }

    private void init() {
        //初始化一些信息，比如如request，方便在多线程中调用
        requestThreadLocal.set(HttpServletUtil.signle().getHttpServlet());
        responseThreadLocal.set(HttpServletUtil.signle().getHttpServletResponse());
    }

    private void unInit() {
        //结束后要将初始化的信息清空
        requestThreadLocal.set(null);
        responseThreadLocal.set(null);
    }

    @Override
    public V call() throws Exception {
        init();
        V result = process();
        unInit();
        return result;
    }

    @Override
    public void run() {
        init();
        process();
        unInit();
    }

    public abstract V process();


}
