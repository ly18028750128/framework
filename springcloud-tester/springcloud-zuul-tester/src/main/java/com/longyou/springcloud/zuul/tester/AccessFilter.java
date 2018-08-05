package com.longyou.springcloud.zuul.tester;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class AccessFilter extends ZuulFilter
{
    
    @Override
    public Object run()
    {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public boolean shouldFilter()
    {
        // TODO Auto-generated method stub
        return true;
    }
    
    @Override
    public int filterOrder()
    {
        // TODO Auto-generated method stub
        return 0;
    }
    
    @Override
    public String filterType()
    {
        // TODO Auto-generated method stub
        return "pre";
    }
    
}
