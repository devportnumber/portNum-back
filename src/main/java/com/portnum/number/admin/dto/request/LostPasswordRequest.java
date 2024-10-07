package com.portnum.number.admin.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class LostPasswordRequest {

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String email;
}
