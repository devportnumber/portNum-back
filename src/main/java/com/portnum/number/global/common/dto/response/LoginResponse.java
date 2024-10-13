package com.portnum.number.global.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResponse{

    private Long adminId;
    private String loginId;
    private String email;
    private String nickName;
    private String profileUrl;
    private Boolean isRqPwChange;
    private String urlName;
}