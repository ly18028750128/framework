package org.cloud.utils;


import feign.FeignException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.cloud.entity.LoginUserDetails;
import org.cloud.feign.service.IGatewayFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.client.HttpClientErrorException;

public final class CommonUtil {

    final Logger logger = LoggerFactory.getLogger(CommonUtil.class);
    final IGatewayFeignClient gatewayFeignClient;
    final Environment env;

    @Lazy
    private CommonUtil() {
        env = SpringContextUtil.getBean(Environment.class);
        gatewayFeignClient = SpringContextUtil.getBean(IGatewayFeignClient.class);
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
        return env.getProperty(key, defaultValue);
    }

    public String getEnv(String key) {
        return env.getProperty(key);
    }

    public <T> T getEnv(String key, T defaultValue, Class cls) {
        final String value = getEnv(key);
        if (value == null) {
            return defaultValue;
        }

        try {
            return (T) cls.getConstructor(String.class).newInstance(value);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T getEnv(String key, Class cls) {
        return getEnv(key, null, cls);
    }


    /**
     * 获取当前登录用户
     *
     * @return
     */
    public LoginUserDetails getLoginUser() {
        try {
            return gatewayFeignClient.getAuthentication();
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.debug("rest请求无权限");
        } catch (FeignException.Unauthorized e) {
            logger.debug("feign请求无权限");
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() != HttpStatus.UNAUTHORIZED.value()) {
                logger.error(e.getMessage(), e);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
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

    public String getStackTrace(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        try {
            throwable.printStackTrace(pw);
            return sw.toString();
        } finally {
            pw.close();
        }
    }

    /**
     * 将map转换成查询参数
     *
     * @param paramMap
     * @return
     */
    public String convertMapToQueryParams(Map<String, String> paramMap) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String key : paramMap.keySet()) {
            try {
                stringBuffer.append(key + "=" + URLEncoder.encode(paramMap.get(key), "utf8"));
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage());
            }
        }
        return stringBuffer.toString();

    }

    public String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.contains(",")) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if ("0:0:0:0:0:0:0:1".equals(ip)) {
            return "127.0.0.1";
        }

        return ip;
    }
}
