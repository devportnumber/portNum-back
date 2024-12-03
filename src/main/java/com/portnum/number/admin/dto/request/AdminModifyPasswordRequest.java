package com.portnum.number.admin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class AdminModifyPasswordRequest {

    @NotNull
    private Long adminId;

    private String oldPassword;

    @NotBlank
    private String newPassword;
}
