package com.portnum.number.global.exception;

import com.portnum.number.global.common.dto.response.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

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
        return ErrorResponseDto.of(Code.VALIDATION_ERROR, "잘못된 값 전달");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponseDto handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException: {}", e.getMessage());
        return ErrorResponseDto.of(Code.INVALID_JSON_FORMAT, "JSON 형식 에러");
    }

    @ExceptionHandler(IOException.class)
    public ErrorResponseDto handleIOException(IOException e) {
        log.error("IOException: {}", e.getMessage());
        return ErrorResponseDto.of(Code.IO_ERROR, "IO Error");
    }

//    @ExceptionHandler(RuntimeException.class)
//    public ErrorResponseDto handleRuntimeException(RuntimeException e) {
//        log.error("RuntimeException : {}", e.getMessage());
//        return ErrorResponseDto.of(Code.INTERNAL_ERROR, "서버 내부 에러");
//    }
}