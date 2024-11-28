package com.portnum.number.popup.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PopupRemoveRequest {

    @NotNull
    private List<Long> popupIds;

    @NotNull
    private Long adminId;

}
