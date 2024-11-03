package com.portnum.number.global.exception;

import com.portnum.number.global.exception.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;


public class JwtException extends FilterException {
    public JwtException(Code errorCode, String message) {
        super(errorCode, message);
    }
}
