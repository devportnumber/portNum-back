package com.portnum.number.global.common.enums;


import lombok.Getter;

@Getter
public enum RedisPrefixEnum {
    POPUP("popup:");

    private final String prefix;

    RedisPrefixEnum(String prefix) {
        this.prefix = prefix;
    }
}
