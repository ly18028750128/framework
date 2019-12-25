package org.cloud.exception.handler;

import org.cloud.exception.BusinessException;
import org.cloud.vo.ResponseResult;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobExceptionHandler extends ResponseEntityExceptionHandler {

    final  Logger logger = LoggerFactory.getLogger(GlobExceptionHandler.class);

    @ExceptionHandler(HttpClientErrorException.class)
    public Map<String,Object> handlerHttpClientErrorException(@NotNull HttpClientErrorException e, @NotNull HttpServletResponse response){
        ResponseResult responseResult = ResponseResult.createFailResult();
        responseResult.setMessage(e.getMessage());
        response.setStatus(e.getStatusCode().value());
        logger.error(e.getMessage(),e);
        return responseResult;
    }

    @ExceptionHandler(RuntimeException.class)
    public Map<String,Object> handlerHttpClientErrorException(@NotNull RuntimeException e, @NotNull HttpServletResponse response){
        ResponseResult responseResult = ResponseResult.createFailResult();
        responseResult.setMessage(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        logger.error(e.getMessage(),e);
        return responseResult;
    }

    @ExceptionHandler(BusinessException.class)
    public Map<String,Object> handlerBusinessException(@NotNull BusinessException e,@NotNull HttpServletResponse response){
        ResponseResult responseResult = ResponseResult.createFailResult();
        responseResult.setMessage(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        logger.error(e.getMessage(),e);
        return responseResult;
    }

    @ExceptionHandler(Exception.class)
    public Map<String,Object> handlerException(@NotNull Exception e, @NotNull HttpServletResponse response){
        ResponseResult responseResult = ResponseResult.createFailResult();
        responseResult.setMessage(e.getMessage());
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        logger.error(e.getMessage(),e);
        return responseResult;
    }



}
