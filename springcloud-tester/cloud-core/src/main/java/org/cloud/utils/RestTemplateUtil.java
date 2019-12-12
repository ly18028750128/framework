package org.cloud.utils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.util.Enumeration;

public class RestTemplateUtil {

    private RestTemplate restTemplate;

    private RestTemplateUtil() {
        restTemplate = (RestTemplate) SpringContextUtil.getBean(RestTemplate.class);
    }

    private static class Handler {
        private Handler() {
        }

        private static RestTemplateUtil util = new RestTemplateUtil();
    }

    public static RestTemplateUtil single() {
        return Handler.util;
    }

//    public ResponseEntity<String> getResponse(final String url, final HttpMethod method, String requestBody, HttpHeaders headers) {
//        HttpEntity httpEntity = null;
//        if (StringUtils.isEmpty(requestBody)) {
//            httpEntity = new HttpEntity<String>("", headers);
//        } else {
//            httpEntity = new HttpEntity<String>(headers);
//        }
//        RequestCallback requestCallback = restTemplate.httpEntityCallback(httpEntity, String.class);
//        ResponseExtractor<ResponseEntity<String>> responseExtractor = restTemplate.responseEntityExtractor(String.class);
//        ResponseEntity<String> response = restTemplate.execute(url, method, requestCallback, responseExtractor);
//        return response;
//    }

    public <T> ResponseEntity<T> getResponse(final String url, final HttpMethod method, String requestBody, HttpHeaders headers, Class<T> responseType) {
        HttpEntity httpEntity = null;
        if (StringUtils.isEmpty(requestBody)) {
            httpEntity = new HttpEntity<String>("", headers);
        } else {
            httpEntity = new HttpEntity<String>(headers);
        }
        RequestCallback requestCallback = restTemplate.httpEntityCallback(httpEntity, responseType);
        ResponseExtractor<ResponseEntity<String>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        ResponseEntity<T> response = (ResponseEntity<T>) restTemplate.execute(url, method, requestCallback, responseExtractor);
        return response;
    }

    public <T> ResponseEntity<T> getResponse(final String url, final HttpMethod method, String requestBody, Class<T> responseType) {
        return getResponse(url, method, requestBody, null, responseType);
    }

    public <T> ResponseEntity<T> getResponse(final String url, final HttpMethod method, HttpHeaders headers, Class<T> responseType) {
        return getResponse(url, method, null, headers, responseType);
    }

    public <T> ResponseEntity<T> getResponse(final String url, final HttpMethod method, Class<T> responseType) {
        return getResponse(url, method, null, null, responseType);
    }

//    public String getJson(final String url, final HttpMethod method, String requestBody, HttpHeaders headers) {
//        ResponseEntity<String> response = getResponse(url, method, requestBody, headers);
//        return response.getBody();
//    }
//
//    public <T> T execute(final String url, final HttpMethod method, String requestBody, HttpHeaders headers, Class<T> cls) {
//        String jsonResult = this.getJson(url, method, requestBody, headers);
//        return JSON.parseObject(jsonResult, cls);
//    }

    public HttpHeaders getHttpHeadersFromHttpRequest(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            headers.add(key, value);
        }
        return headers;
    }


}
