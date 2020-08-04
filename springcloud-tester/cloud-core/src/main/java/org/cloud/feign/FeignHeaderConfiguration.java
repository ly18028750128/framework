package org.cloud.feign;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.cloud.utils.HttpServletUtil;
import org.cloud.utils.RestTemplateUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * 需要传传递token里，配置feign类时需要引用
 * 自定义的请求头处理类，处理服务发送时的请求头；
 * 将服务接收到的请求头中的uniqueId和token字段取出来，并设置到新的请求头里面去转发给下游服务
 * 比如A服务收到一个请求，请求头里面包含uniqueId和token字段，A处理时会使用Feign客户端调用B服务
 * 那么uniqueId和token这两个字段就会添加到请求头中一并发给B服务；
 *
 * @author Curise
 * @create 2018/11/20
 * @since 1.0.0
 */
@Configuration
@Slf4j
public class FeignHeaderConfiguration {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            HttpServletRequest request = HttpServletUtil.signle().getHttpServlet();
            if (request == null) {
                return;
            }
            HttpHeaders headers = RestTemplateUtil.single().getHttpHeadersFromHttpRequest(request, new String[]{"authorization", "cookie"});
            Map<String, Collection<String>> headersMap = new HashMap<>();
            headersMap.putAll(headers);
            requestTemplate.headers(headersMap);

        };
    }
}
