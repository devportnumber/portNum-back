package com.portnum.number.admin.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LostPasswordRequest {

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String email;
}
