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

    private long timeSaltPre= System.currentTimeMillis(); //上一个时间盐的值
    public String getTimeSalt(){
        final String timeSaltChangeInterval=this.getEnv("system.time_salt_change_interval",Long.toString(5*60*1000));
        final long  lTimeSaltChangeInterval = Long.valueOf(timeSaltChangeInterval);
        if(System.currentTimeMillis() - timeSaltPre > lTimeSaltChangeInterval){
            timeSaltPre = System.currentTimeMillis();
        }
        return Long.toString(timeSaltPre);
    }

}
