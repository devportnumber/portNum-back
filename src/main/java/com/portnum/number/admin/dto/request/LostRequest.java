package com.portnum.number.admin.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class LostRequest {

    @NotEmpty
    private String nickName;

    @NotEmpty
    private String email;
}
