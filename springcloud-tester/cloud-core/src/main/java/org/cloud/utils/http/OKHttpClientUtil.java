package org.cloud.utils.http;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.cloud.utils.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class OKHttpClientUtil {

    Logger logger = LoggerFactory.getLogger(OKHttpClientUtil.class);

    private static final OKHttpClientUtil singleInstance = new OKHttpClientUtil();

    public static OKHttpClientUtil single() {
        return singleInstance;
    }

    private final OkHttpClient okHttpClient;

    private OKHttpClientUtil() {
        okHttpClient = OKHttpClientBuilder.buildOKHttpClient().build();
        okHttpClient.dispatcher().setMaxRequests(10000);
        okHttpClient.dispatcher().setMaxRequestsPerHost(5000);  //每台服务器最大5000个请求
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    // 获取数据进行异步的call，针对get请求
    public Call createGetCall(HttpRequestParams httpRequestParams, String url) throws Exception {
        Request.Builder request = new Request.Builder();
        // 设置header头
        httpRequestParams.getHeaders().forEach((key, value) -> {
            if (value != null) {
                request.addHeader(key, value);
            }
        });
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        httpRequestParams.getQueryParams().forEach((key, value) -> {
            if (key != null && value != null) {
                urlBuilder.addQueryParameter(key, value.toString());
            }
        });
        request.url(urlBuilder.build());
        return okHttpClient.newCall(request.build());
    }

    public Call createPostCall(HttpRequestParams httpRequestParams, String url) throws Exception {
        MediaType mediaTypeJSON = MediaType.parse("application/json; charset=utf-8");
        return createPostCall(httpRequestParams,url,mediaTypeJSON);
    }

    // 获取数据进行异步的call，针对post请求
    public Call createPostCall(HttpRequestParams httpRequestParams, String url,MediaType mediaType) throws Exception {
        Request.Builder request = new Request.Builder();
        // 设置header头
        httpRequestParams.getHeaders().forEach((key, value) -> {
            if (value != null) {
                request.addHeader(key, value);
            }
        });
        // 如果requestbody为空，那么将查询参数按form表单的形式提交
        if (httpRequestParams.getRequestBody() == null || httpRequestParams.getRequestBody().isEmpty()) {
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            if (httpRequestParams.getQueryParams() != null) {
                httpRequestParams.getQueryParams().forEach((key, value) -> {
                    if (key != null && value != null) {
                        formBodyBuilder.add(key, value.toString());
                    }
                });
            }
            FormBody formBody = formBodyBuilder.build();
            request.url(url);
            request.post(formBody);
        } else {
            //如果getRequestBody不为空那么将其添加到参数中去
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
            if (httpRequestParams.getQueryParams() != null && !httpRequestParams.getQueryParams().isEmpty()) {
                httpRequestParams.getQueryParams().forEach((key, value) -> {
                    if (key != null && value != null) {
                        urlBuilder.addQueryParameter(key, value.toString());
                    }
                });
            }
            request.url(urlBuilder.build());

            if(mediaType.type().equals(MediaType.parse("application/xml").type())){
                request.post(RequestBody.create(XmlUtil.Object2XmlString(httpRequestParams.getRequestBody()), mediaType));
            }else{
                request.post(RequestBody.create(JSON.toJSONString(httpRequestParams.getRequestBody()), mediaType));
            }
        }
        return okHttpClient.newCall(request.build());
    }

    // 同步请求GET请求
    public String getResponse(HttpRequestParams httpRequestParams, String url) throws Exception {
        ResponseBody body = createGetCall(httpRequestParams, url).execute().body();
        String result = body.string();
        body.close();
        return result;
    }

    // 同步发送POST请求
    public String postResponse(HttpRequestParams httpRequestParams, String url) throws Exception {
        ResponseBody body =  createPostCall(httpRequestParams, url).execute().body();
        String result = body.string();
        body.close();
        return result;
    }

    public WebSocket connectionWebsocket(String url, WebSocketListener webSocketListener) {
        Request request = new Request.Builder().url(url).build();
        final WebSocket websocket = getOkHttpClient().newWebSocket(request, webSocketListener);
        getOkHttpClient().dispatcher().executorService().shutdown();
        return websocket;
    }
}
