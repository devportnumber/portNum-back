package com.portnum.number.admin.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LostPasswordRequest {

    @NotEmpty
    private String loginId;

    @NotEmpty
    private String email;
}
