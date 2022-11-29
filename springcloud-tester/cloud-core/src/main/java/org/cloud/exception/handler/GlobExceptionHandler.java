package org.cloud.exception.handler;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import org.cloud.exception.BusinessException;
import org.cloud.utils.CommonUtil;
import org.cloud.vo.CommonApiResult;
import org.cloud.vo.ResponseResult;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobExceptionHandler extends ResponseEntityExceptionHandler {

    final Logger logger = LoggerFactory.getLogger(GlobExceptionHandler.class);

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseResult<?> handlerHttpClientErrorException(@NotNull HttpClientErrorException e, @NotNull HttpServletResponse response) {
        return getStringObjectMap(e, response, e.getStatusCode().value());
    }

    @ExceptionHandler(ServletException.class)
    public ResponseResult<?> handlerServletException(@NotNull ServletException e, @NotNull HttpServletResponse response) {
        return getStringObjectMap(e, response);
    }

    @ExceptionHandler(IOException.class)
    public ResponseResult<?> handlerIOException(@NotNull IOException e, @NotNull HttpServletResponse response) {
        return getStringObjectMap(e, response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseResult<?> handlerHttpClientErrorException(@NotNull RuntimeException e, @NotNull HttpServletResponse response) {
        return getStringObjectMap(e, response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult<?> handlerException(@NotNull Exception e, @NotNull HttpServletResponse response) {
        return getStringObjectMap(e, response);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseResult<?> handlerException(@NotNull Throwable e, @NotNull HttpServletResponse response) {
        return getStringObjectMap(e, response);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseResult<?> handlerBusinessException(@NotNull BusinessException e, @NotNull HttpServletResponse response) {
        ResponseResult<?> responseResult = ResponseResult.createFailResult();
        responseResult.setMessage(e.getMessage());
        responseResult.setData(e.getErrObject());
        response.setStatus(e.getHttpStatus());
        logger.error(CommonUtil.single().getStackTrace(e));
        return responseResult;
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
        return handleExceptionInternal(ex, CommonApiResult.createFailResult(ex.getMessage()), headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatus status,
        WebRequest request) {
        return handleExceptionInternal(ex, CommonApiResult.createFailResult(ex.getMessage()), headers, HttpStatus.BAD_REQUEST, request);
    }

    @NotNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NotNull HttpHeaders headers, @NotNull HttpStatus status,
        @NotNull WebRequest request) {
        List<?> errorList = ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        return handleExceptionInternal(ex, CommonApiResult.createFailResult(errorList), headers, HttpStatus.BAD_REQUEST, request);
    }

    @NotNull
    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, @NotNull HttpHeaders headers, @NotNull HttpStatus status,
        @NotNull WebRequest request) {
        ex.getAllErrors();
        List<?> errorList = ex.getBindingResult().getFieldErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.toList());
        return handleExceptionInternal(ex, CommonApiResult.createFailResult(errorList), headers, HttpStatus.BAD_REQUEST, request);
    }


    @ExceptionHandler(SQLException.class)
    public ResponseResult<?> handlerSQLException(@NotNull SQLException e, @NotNull HttpServletResponse response) {
        return getStringObjectMap(e, response);
    }

    private ResponseResult<?> getStringObjectMap(@NotNull Throwable e, @NotNull HttpServletResponse response, int httpStatus) {
        if (e.getCause() != null && e.getCause() instanceof BusinessException) {
            return this.handlerBusinessException((BusinessException) e.getCause(), response);
        }

        ResponseResult<?> responseResult = ResponseResult.createFailResult();

        if ((e instanceof java.sql.SQLException)) {
            responseResult.setMessage(e.getMessage());
        } else if ((e.getCause() != null && e.getCause() instanceof java.sql.SQLException)) {
            responseResult.setMessage(e.getCause().getMessage());
        } else if ((e instanceof DataAccessException) || (e.getCause() != null && e.getCause() instanceof DataAccessException)) {
            responseResult.setMessage("system.db.error");
        } else {
            responseResult.setMessage(e.getMessage());
        }

        response.setStatus(httpStatus);
        logger.error(CommonUtil.single().getStackTrace(e));
        return responseResult;
    }

    private ResponseResult<?> getStringObjectMap(@NotNull Throwable e, @NotNull HttpServletResponse response) {
        return getStringObjectMap(e, response, HttpStatus.BAD_REQUEST.value());
    }
}
