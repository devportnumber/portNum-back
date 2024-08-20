package com.portnum.number.global.common.dto.response;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PreSignedUrlResponse {

    private String preSingedUrl;
    private String imageSaveUrl;

    public static PreSignedUrlResponse of(String preSingedUrl, String imageSaveUrl){
        return PreSignedUrlResponse.builder()
                .preSingedUrl(preSingedUrl)
                .imageSaveUrl(imageSaveUrl)
                .build();
    }
}
