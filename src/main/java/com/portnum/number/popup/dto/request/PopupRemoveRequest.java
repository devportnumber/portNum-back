package com.portnum.number.popup.dto.request;

import com.portnum.number.popup.entity.Popup;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class PopupRemoveRequest {

    @NotNull
    private List<Long> popupIds;

    @NotNull
    private Long adminId;

}
