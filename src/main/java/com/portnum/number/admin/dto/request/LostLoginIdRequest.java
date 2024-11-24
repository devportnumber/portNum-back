package com.portnum.number.admin.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LostLoginIdRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String email;
}
