package com.portnum.number.global.common.enums;

import lombok.Getter;

@Getter
public enum ExpiredTimeEnum {
    ACCESS_TOKEN(30 * 60 * 1000L), // 30분
    REFRESH_TOKEN(60 * 60 * 3 * 1000L); // 30분

    private final long expiredTime;

    ExpiredTimeEnum(long expiredTime) {
        this.expiredTime = expiredTime;
    }
}
