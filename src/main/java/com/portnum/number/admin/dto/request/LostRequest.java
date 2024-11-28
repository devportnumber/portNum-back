package com.portnum.number.admin.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LostRequest {

    @NotEmpty
    private String nickName;

    @NotEmpty
    private String email;
}
