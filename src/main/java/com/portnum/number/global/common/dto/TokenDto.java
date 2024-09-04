package com.portnum.number.global.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class TokenDto {
    private String grantType; // JWT에 대한 인증 타입. Bearer를 사용
    private final String authorizationType;
    private String accessToken;
    private String refreshToken;
    private final Long accessTokenExpiresIn;
}
