package com.portnum.number.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdminModifyPasswordRequest {

    @NotNull
    private Long adminId;

    private String oldPassword;

    @NotBlank
    private String newPassword;
}
