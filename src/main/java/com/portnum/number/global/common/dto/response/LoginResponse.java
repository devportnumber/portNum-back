package com.portnum.number.global.common.dto.response;

import com.portnum.number.global.security.custom.CustomUserDetails;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginResponse{

    private Long adminId;
    private String loginId;
    private String email;
    private String nickName;
    private String profileUrl;
    private Boolean isRqPwChange;
    private String urlName;

    public static LoginResponse from(CustomUserDetails customUserDetails){
        return LoginResponse.builder()
                .adminId(customUserDetails.getId())
                .loginId(customUserDetails.getLoginId())
                .email(customUserDetails.getEmail())
                .nickName(customUserDetails.getEmail())
                .profileUrl(customUserDetails.getProfileUrl())
                .isRqPwChange(customUserDetails.getIsRqPwChange())
                .urlName(customUserDetails.getUrlName())
                .build();
    }
}