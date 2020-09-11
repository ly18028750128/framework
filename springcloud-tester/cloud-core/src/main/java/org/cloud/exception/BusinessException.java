package org.cloud.exception;

import lombok.Getter;

@Getter
public class BusinessException extends Exception {

    private Object errObject = null;
    private int httpStatus;
    private String message;


    public BusinessException(String message, Object errObject) {
        this.message = message;
        this.errObject = errObject;
        this.httpStatus = 400;
    }

    public BusinessException(String message) {
        this.message = message;
        this.errObject = null;
        this.httpStatus = 400;
    }

    public BusinessException(String message, Throwable cause, int httpStatus) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    public BusinessException(String message, Object errObject, int httpStatus) {
        this.message = message;
        this.errObject = errObject;
        this.httpStatus = httpStatus;
    }


}
