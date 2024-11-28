package com.portnum.number.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AccountException extends FilterException{
    public AccountException(Code errorCode, String message) {
        super(errorCode, message);
    }
}
