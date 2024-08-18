package com.portnum.number.admin.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AdminCreateRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String nickName;

    @NotBlank
    private String name;

    private String profileUrl;

    @NotBlank
    private String phone;
}
