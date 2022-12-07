package org.cloud.vo;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import lombok.Data;
import org.cloud.constant.CoreConstant;

@Data
public class CommonApiResult<T> {

    private HashMap<String,Object> otherInfo = new LinkedHashMap<>(2);

    private int status;

    private int code;

    private Object errResultData;

    private String message;

    private T data;


    public CommonApiResult() {
//        put(_STATUS_CODE_KEY, CoreConstant.RestStatus.SUCCESS.value());
        this.status = CoreConstant.RestStatus.SUCCESS.value();
    }

    public CommonApiResult(int status) {

        this.status = status;
        this.code = status;
    }

    public CommonApiResult(Collection<?> data) {
        this.data = (T) data;
    }

    public CommonApiResult(int status, T data) {
        this.status = status;
        this.code = status;
        this.data = data;
    }

    public static <T> CommonApiResult<T> createFailResult() {
        CommonApiResult<T> responseResult = new CommonApiResult<>(CoreConstant.RestStatus.FAIL.value());
        responseResult.setMessage("rest.failed.running");
        return responseResult;
    }

    public static <T> CommonApiResult<T> createFailResult(T data) {
        CommonApiResult<T> responseResult = new CommonApiResult<>(CoreConstant.RestStatus.FAIL.value());
        responseResult.setMessage("rest.failed.running");
        responseResult.setData(data);
        return responseResult;
    }

    public static <T> CommonApiResult<T> createSuccessResult() {
        CommonApiResult<T> responseResult = new CommonApiResult<>(CoreConstant.RestStatus.SUCCESS.value());
        responseResult.setMessage("rest.success.running");
        return responseResult;
    }

    public static <T> CommonApiResult<T> createSuccessResult(T data) {
        CommonApiResult<T> responseResult = new CommonApiResult<>(CoreConstant.RestStatus.SUCCESS.value());
        responseResult.setMessage("rest.success.running");
        responseResult.setData(data);
        return responseResult;
    }

    public static <T> CommonApiResult<T> createSuccessResult(Collection<?> data) {
        CommonApiResult<T> responseResult = new CommonApiResult<>(CoreConstant.RestStatus.SUCCESS.value());
        responseResult.setMessage("rest.success.running");
        responseResult.setData((T) data);
        return responseResult;
    }


    public void setErrResultData(Object value) {
        this.errResultData = value;
    }

    public void setStatus(CoreConstant.RestStatus value) {
        this.status = value.value();
        this.code = value.value();
    }

}
