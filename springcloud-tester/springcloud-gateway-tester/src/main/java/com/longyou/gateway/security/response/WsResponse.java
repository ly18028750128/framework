package com.longyou.gateway.security.response;

import static com.longyou.gateway.security.response.MessageCode.COMMON_SUCCESS;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class WsResponse<T> {

    private MessageCode status;
    private List<String> messages;
    private T result;

    public WsResponse() {
        messages = new ArrayList<>();
    }

    public WsResponse(MessageCode status, T result) {
        messages = new ArrayList<>();
        this.getMessages().add(COMMON_SUCCESS.message);
        this.status = status;
        this.result = result;
    }

    public List<String> getMessages() {
        return messages;
    }

    public static <T> WsResponse<T> failure(String msg) {
        WsResponse<T> resp = new WsResponse<>();
        resp.status = MessageCode.COMMON_FAILURE;
        resp.getMessages().add(msg);
        return resp;
    }

    public static <T> WsResponse<T> failure(MessageCode messageCode) {
        WsResponse<T> resp = new WsResponse<>();
        resp.status = messageCode;
        resp.getMessages().add(messageCode.message);
        return resp;
    }

    public static <T> WsResponse<T> failure(MessageCode messageCode, String message) {
        WsResponse<T> resp = new WsResponse<>();
        resp.status = messageCode;
        if (StrUtil.isNotBlank(messageCode.message)) {
            resp.getMessages().add(messageCode.message);
        }
        if (StrUtil.isNotBlank(message)) {
            resp.getMessages().add(message);
        }
        return resp;
    }

    public static <T> WsResponse<T> success() {
        WsResponse<T> resp = new WsResponse<>();
        resp.status = COMMON_SUCCESS;
        resp.getMessages().add(COMMON_SUCCESS.message);
        return resp;
    }

    public static <K> WsResponse<K> success(K t) {
        WsResponse<K> resp = new WsResponse<>();
        resp.status = COMMON_SUCCESS;
        resp.getMessages().add(COMMON_SUCCESS.message);
        resp.result = t;
        return resp;
    }
}
