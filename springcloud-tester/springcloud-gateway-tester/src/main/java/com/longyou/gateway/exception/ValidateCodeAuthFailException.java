package com.longyou.gateway.exception;


import org.springframework.security.core.AuthenticationException;

public class ValidateCodeAuthFailException extends AuthenticationException {

    public ValidateCodeAuthFailException(String msg, Throwable t) {
        super(msg, t);
    }

    public ValidateCodeAuthFailException(String msg) {
        super(msg);
    }
}
