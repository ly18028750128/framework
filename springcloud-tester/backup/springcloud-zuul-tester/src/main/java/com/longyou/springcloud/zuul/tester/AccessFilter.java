package com.longyou.springcloud.zuul.tester;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AccessFilter extends ZuulFilter {
    final Logger logger = LoggerFactory.getLogger(AccessFilter.class);

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();


        logger.debug("session id is " + request.getSession().getId());


        // TODO Auto-generated method stub
        return null;
    }

//    public 

    @Override
    public boolean shouldFilter() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public int filterOrder() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String filterType() {
        // TODO Auto-generated method stub
        return "pre";
    }

}
