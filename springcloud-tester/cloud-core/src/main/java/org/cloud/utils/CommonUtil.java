package org.cloud.utils;


import org.apache.catalina.util.RequestUtil;
import org.cloud.entity.LoginUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public final class CommonUtil {

    Logger logger = LoggerFactory.getLogger(CommonUtil.class);

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
     *
     * @param request
     * @return
     */
    public LoginUserDetails getLoginUser(HttpServletRequest request) {
        try {
            final String userinfoUrl = this.getEnv("system.userinfo.get_url", "http://SPRING-GATEWAY/user/info/authentication");
            HttpHeaders headers = RestTemplateUtil.single().getHttpHeadersFromHttpRequest(request);
            ResponseEntity<LoginUserDetails> responseEntity = RestTemplateUtil.single().getResponse(userinfoUrl, HttpMethod.GET, headers, LoginUserDetails.class);
            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() != HttpStatus.UNAUTHORIZED.value()) {
                logger.error(e.getMessage(), e);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public LoginUserDetails getLoginUser() {
        return getLoginUser(HttpServletUtil.signle().getHttpServlet());
    }

    private long timeSaltPre = System.currentTimeMillis(); //上一个时间盐的值

    public String getTimeSalt() {
        final String timeSaltChangeInterval = this.getEnv("system.time_salt_change_interval", Long.toString(5 * 60 * 1000));
        final long lTimeSaltChangeInterval = Long.valueOf(timeSaltChangeInterval);
        if (System.currentTimeMillis() - timeSaltPre > lTimeSaltChangeInterval) {
            timeSaltPre = System.currentTimeMillis();
        }
        return Long.toString(timeSaltPre);
    }

    /**
     * 匹配Url
     *
     * @param uris
     * @param requestUri
     * @return
     */
    public boolean pathMatch(String requestUri, List<String> uris) {
        for (String exclude : uris) {
            final PathMatcher pathMathcer = new AntPathMatcher();
            if (pathMathcer.match(exclude, requestUri)) {
                return true;
            }
        }
        return false;
    }

}
