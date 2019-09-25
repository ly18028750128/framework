package org.cloud.context;

import com.alibaba.fastjson.JSON;
import org.cloud.utils.process.ProcessCallable;
import org.cloud.utils.process.ProcessUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static org.junit.Assert.*;

public class RequestContextManagerTest {

    private Logger logger = LoggerFactory.getLogger(RequestContextManagerTest.class);

    @Test
    public void getRequestContext() {

        final RequestContext requestContext = new RequestContext();
        RequestContextManager.single().setRequestContext(requestContext);
        List<Callable<Object>> callables = new ArrayList<>();

        callables.add(new ProcessCallable(true) {
            @Override
            public Object process() {
                RequestContextManager.single().getRequestContext().setProperty("threadName","1");
                RequestContextManager.single().getRequestContext().setProperty("threadNameValue","1");
                logger.info(JSON.toJSONString(RequestContextManager.single().getRequestContext().getProperties()));
                return null;
            }
        });

        callables.add(new ProcessCallable(true) {
            @Override
            public Object process() {
                RequestContextManager.single().getRequestContext().setProperty("threadName","2");
                RequestContextManager.single().getRequestContext().setProperty("threadNameValue","2");
                logger.info(JSON.toJSONString(RequestContextManager.single().getRequestContext().getProperties()));
                return null;
            }
        });

        ProcessUtil.single().runCablles(callables);
    }
}