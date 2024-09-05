package com.portnum.number.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class AdminModifyRequest {

    private Long adminId;

    private String nickName;

    private String name;

    private String profileUrl;
}
