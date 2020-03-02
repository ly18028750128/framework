package org.cloud.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends Exception {

    private Object errObject = null ;
    private int httpStatus;
    private String message;


    public BusinessException(String message, Object errObject) {
        this.message = message;
        this.errObject = errObject;
        this.httpStatus = 400;
    }

    public BusinessException(String message, Object errObject,int httpStatus) {
        this.message = message;
        this.errObject = errObject;
        this.httpStatus = httpStatus;
    }

}
