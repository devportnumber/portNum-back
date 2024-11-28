package com.portnum.number.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GlobalException extends RuntimeException {
    // errorCode에 대한 getter
    private final Code errorCode;
    private final String message;
}
