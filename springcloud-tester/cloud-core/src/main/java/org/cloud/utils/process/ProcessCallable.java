package org.cloud.utils.process;

import org.cloud.context.RequestContext;
import org.cloud.context.RequestContextManager;
import org.cloud.utils.HttpServletUtil;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.Callable;

public abstract class ProcessCallable<V> implements Callable, Runnable {

    private RequestContext requestContext;

    public ProcessCallable() {
        this.requestContext = RequestContextManager.single().getRequestContext();
    }

    /**
     * 是否克隆当前的上下文
     *
     * @param isCloneCurrentRequestContext
     */
    public ProcessCallable(Boolean isCloneCurrentRequestContext) {
        if (isCloneCurrentRequestContext) {
            this.requestContext = RequestContextManager.single().cloneRequestContext();
        } else {
            this.requestContext = RequestContextManager.single().getRequestContext();
        }
    }

    public ProcessCallable(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    private void init() {
        //初始化一些信息，比如如request，方便在多线程中调用
        RequestContextManager.single().setRequestContext(requestContext);
    }

    private void unInit() {
        //结束后要将初始化的信息清空
        RequestContextManager.single().setRequestContext(null);
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
