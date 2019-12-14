package org.cloud.utils.http;

import java.util.LinkedHashMap;
import java.util.Map;

public class HttpRequestParams {

    private String requestMethod; // get,post,put,delete

    private Map<String, String> headers;   //请求头
    private Map<String, Object> queryParams;    //查询参数
    private Map<String, Object> requestBody;   //请求体

    public HttpRequestParams() {
    }


    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, Object> queryParams) {
        this.queryParams = queryParams;
    }

    public Map<String, String> getHeaders() {
        if (headers == null) {
            headers = new LinkedHashMap<>();
        }
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }


    public Map<String, Object> getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(Map<String, Object> requestBody) {
        this.requestBody = requestBody;
    }

}
