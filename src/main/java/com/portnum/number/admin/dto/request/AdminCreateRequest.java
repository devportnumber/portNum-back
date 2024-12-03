package com.portnum.number.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminCreateRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String loginId;

    @NotBlank
    private String nickName;

    @NotBlank
    private String name;

    private String profileUrl;

    @NotBlank
    private String password;

    public void modifyPassword(String password){
        this.password = password;
    }
}
