package com.portnum.number.global.exception;

import com.portnum.number.global.common.dto.response.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class PortNumberExceptionHandler{

    @ExceptionHandler(GlobalException.class)
    protected ErrorResponseDto handleGlobalException(GlobalException e){
        log.error("GlobalException: {}", e.getErrorCode().getMessage());
        return ErrorResponseDto.of(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponseDto handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException: {}", e.getDetailMessageArguments());
        return ErrorResponseDto.of(Code.VALIDATION_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponseDto handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException: {}", e.getMessage());
        return ErrorResponseDto.of(Code.INVALID_JSON_FORMAT, "Invalid Json format");
    }
}