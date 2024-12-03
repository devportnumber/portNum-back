package com.portnum.number.popup.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PopupRemoveRequest {

    @NotNull
    private List<Long> popupIds;

    @NotNull
    private Long adminId;

}
