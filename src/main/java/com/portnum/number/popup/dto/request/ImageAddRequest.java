package com.portnum.number.popup.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class ImageAddRequest {

    @NotNull
    private Long adminId;

    @NotNull
    private Long popupId;

    @NotNull
    private List<ImageRequest> images;
}
