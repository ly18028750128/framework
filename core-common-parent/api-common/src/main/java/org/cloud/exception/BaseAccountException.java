package org.cloud.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseAccountException extends Exception {

    public abstract String getErrorCode();

    public int getHttpCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    public BaseAccountException(String message, Throwable cause, boolean enableSuppression,
        boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public BaseAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseAccountException(String message) {
        super(message);
    }

    public BaseAccountException(Throwable cause) {
        super(cause);
    }

    public BaseAccountException() {
        super();
    }

}
