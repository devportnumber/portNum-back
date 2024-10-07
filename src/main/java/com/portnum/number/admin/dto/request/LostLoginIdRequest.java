package com.portnum.number.admin.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class LostLoginIdRequest {

    @NotEmpty
    private String name;

    @NotEmpty
    private String email;
}
