package com.portnum.number.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum Code {

    OK(0, HttpStatus.OK, "Ok"),

    BAD_REQUEST(30000, HttpStatus.BAD_REQUEST, "Bad request"),
    VALIDATION_ERROR(30001, HttpStatus.BAD_REQUEST, "Validation error"),
    NOT_FOUND(30002, HttpStatus.NOT_FOUND, "Requested resource is not found"),

    INTERNAL_ERROR(30010, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),
    DATA_ACCESS_ERROR(30011, HttpStatus.INTERNAL_SERVER_ERROR, "Data access error"),

    UNAUTHORIZED(30020, HttpStatus.UNAUTHORIZED, "User unauthorized"),
    IO_ERROR(30021, HttpStatus.INTERNAL_SERVER_ERROR, "IO_ERROR"),
    FORBIDDEN(30022, HttpStatus.FORBIDDEN, "User Forbidden");


    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;

    public String getMessage(Throwable e) {
        return this.getMessage(this.getMessage() + " - " + e.getMessage());
        // 결과 예시 - "Validation error - Reason why it isn't valid"
    }

    public String getMessage(String message) {
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

    public static Code valueOf(HttpStatus httpStatus) {
        if (httpStatus == null) {
            throw new RuntimeException("HttpStatus is null.");
        }

        return Arrays.stream(values())
                .filter(errorCode -> errorCode.getHttpStatus() == httpStatus)
                .findFirst()
                .orElseGet(() -> {
                    if (httpStatus.is4xxClientError()) {
                        return Code.BAD_REQUEST;
                    } else if (httpStatus.is5xxServerError()) {
                        return Code.INTERNAL_ERROR;
                    } else {
                        return Code.OK;
                    }
                });
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.name(), this.getCode());
    }
}
