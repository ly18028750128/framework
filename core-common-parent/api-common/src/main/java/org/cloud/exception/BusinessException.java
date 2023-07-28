package org.cloud.exception;

import lombok.Getter;

@Getter
public class BusinessException extends Exception {

    private Object errObject = null;
    private final int httpStatus;
    private final String message;

    public BusinessException(String message) {
        super(message);
        this.message = message;
        this.errObject = "";
        this.httpStatus = 400;
    }

    public BusinessException(String message, Object errObject) {
        super(message);
        this.message = message;
        this.errObject = errObject;
        this.httpStatus = 400;
    }


    public BusinessException(String message, Object errObject, int httpStatus) {
        super(message);
        this.message = message;
        this.errObject = errObject;
        this.httpStatus = httpStatus;
    }

    public BusinessException(String message, int httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
        this.errObject = "";
    }

    public BusinessException(String message, Throwable cause, int httpStatus) {
        super(message, cause);
        this.message = message;
        this.httpStatus = httpStatus;
    }

}
