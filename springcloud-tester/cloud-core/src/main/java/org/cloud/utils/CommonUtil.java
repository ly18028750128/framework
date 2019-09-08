package org.cloud.utils;


import org.apache.catalina.util.RequestUtil;
import org.cloud.entity.LoginUserDetails;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;

public final class CommonUtil {
    private CommonUtil() {
    }

    private static class Holder {
        private Holder() {
        }

        private static CommonUtil instance = new CommonUtil();
    }

    public static CommonUtil single() {
        return Holder.instance;
    }

    /**
     * 获取当前配置参数的值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getEnv(String key, String defaultValue) {
        Environment env = SpringContextUtil.getBean(Environment.class);
        return env.getProperty(key, defaultValue);
    }

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    public LoginUserDetails getLoginUser(HttpServletRequest request) {
        final String userinfoUrl = this.getEnv("system.userinfo.get_url", "http://SPRING-GATEWAY/user/info/authentication");
        HttpHeaders headers = RestTemplateUtil.single().getHttpHeadersFromHttpRequest(request);
        LoginUserDetails user = RestTemplateUtil.single().execute(userinfoUrl, HttpMethod.GET, null, headers, LoginUserDetails.class);
        return user;
    }

    public LoginUserDetails getLoginUser() {
        return getLoginUser(HttpServletUtil.signle().getHttpServlet());
    }

}
