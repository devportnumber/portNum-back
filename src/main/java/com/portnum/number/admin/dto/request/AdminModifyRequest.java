package com.portnum.number.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AdminModifyRequest {

    @NotNull
    private Long adminId;

    @NotBlank
    private String nickName;

    @NotBlank
    private String name;

    private String profileUrl;

    @NotBlank
    private String phone;
}
