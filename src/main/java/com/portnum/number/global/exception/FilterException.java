package com.portnum.number.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FilterException extends RuntimeException{
    private final Code errorCode;
    private final String message;
}
