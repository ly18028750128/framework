package org.cloud.context;

import org.cloud.entity.LoginUserDetails;
import org.cloud.entity.Role;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class RequestContext {

    private ThreadLocal<Properties> propertiesThreadLocal = new ThreadLocal<>();

    public RequestContext() {
    }

    public RequestContext(LoginUserDetails user) {
        this.user = user;
    }

    private LoginUserDetails user;

    private HttpServletRequest httpServletRequest;

    private HttpServletResponse httpServletResponse;

    private Map<String,Object> properties=new ConcurrentHashMap<>();

    private List<Role> roles;

    public LoginUserDetails getUser() {
        return user;
    }

    public void setUser(LoginUserDetails user) {
        this.user = user;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    public void setHttpServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    public Map<String,Object> getProperties() {
        return this.properties;
    }

    public void setProperty(String key,Object value) {
        this.properties.put(key,value);
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }


}
