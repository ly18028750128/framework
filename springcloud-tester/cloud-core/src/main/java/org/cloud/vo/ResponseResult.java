package org.cloud.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import org.cloud.constant.CoreConstant;

/**
 * 请使用CommonApiResult
 * @param <E>
 */
@Deprecated
public class ResponseResult<E> extends LinkedHashMap<String, Object> {

    public final static String _HTTP_STATUS_CODE_KEY = "code";
    public final static String _STATUS_CODE_KEY = "status";
    public final static String _MESSAGE_KEY = "message";
    public final static String _ERROR_RESULT_DATA_KEY = "errResultData";   //用来保存错误时的错误数据
    public final static String _DATA_KEY = "data";


    public ResponseResult() {
        put(_STATUS_CODE_KEY, CoreConstant.RestStatus.SUCCESS.value());
    }

    public ResponseResult(int status) {
        put(_STATUS_CODE_KEY, status);
        put(_HTTP_STATUS_CODE_KEY, status);
    }

    public ResponseResult(int status, E data) {
        put(_HTTP_STATUS_CODE_KEY, status);
        put(_STATUS_CODE_KEY, status);
        put(_DATA_KEY, data);
    }

    public static <E> ResponseResult<E> createFailResult() {
        ResponseResult<E> responseResult = new ResponseResult<>(CoreConstant.RestStatus.FAIL.value());
        responseResult.setMessage("rest.failed.running");
        return responseResult;
    }

    public static <E> ResponseResult<E> createSuccessResult() {
        ResponseResult<E> responseResult = new ResponseResult<>(CoreConstant.RestStatus.SUCCESS.value());
        responseResult.setMessage("rest.success.running");
        return responseResult;
    }

    public static <E> ResponseResult<E> createSuccessResult(E data) {
        ResponseResult<E> responseResult = new ResponseResult<>(CoreConstant.RestStatus.SUCCESS.value());
        responseResult.setMessage("rest.success.running");
        responseResult.setData(data);
        return responseResult;
    }

    public static <E> ResponseResult<E> createSuccessResult(Collection<E> data) {
        ResponseResult<E> responseResult = new ResponseResult<>(CoreConstant.RestStatus.SUCCESS.value());
        responseResult.setMessage("rest.success.running");
        responseResult.setData(data);
        return responseResult;
    }

    public void setData(Object value) {
        put(_DATA_KEY, value);
    }

    public <T> T  getData() {
        return (T) get(_DATA_KEY);
    }

    public void setMessage(Object value) {
        put(_MESSAGE_KEY, value);
    }


    /**
     * 批量增加返回数据，如果为空那么直接赋值，如果当前为集合，那么增加到尾部，如果不为集合那么，将原来的值覆盖
     *
     * @param value
     */
    public void addData(E value) {
        Object datas = this.get(_DATA_KEY);
        if (datas == null) {
            this.put(_DATA_KEY, new ArrayList<>());
            datas = this.get(_DATA_KEY);
        }
        if (datas instanceof Collection) {
            Collection<E> dataCollection = ((Collection<E>) datas);
            if (value instanceof Collection<?>) {
                dataCollection.addAll((Collection<E>) value);
            } else {
                dataCollection.add(value);
            }
        } else {
            setData(value);
        }
    }

    public void setErrResultData(Object value) {
        put(_ERROR_RESULT_DATA_KEY, value);
    }

    public void setStatus(CoreConstant.RestStatus value) {
        put(_STATUS_CODE_KEY, value.value());
        put(_HTTP_STATUS_CODE_KEY, value.value());
    }

}
