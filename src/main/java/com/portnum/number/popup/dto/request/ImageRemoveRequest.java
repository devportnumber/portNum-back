package com.portnum.number.popup.dto.request;

import com.portnum.number.popup.dto.response.ImageResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class ImageRemoveRequest {

    @NotNull
    private Long adminId;

    @NotNull
    private Long popupId;

    @NotBlank
    private String representUrl;

    @NotNull
    private List<Long> imgIds;
}
