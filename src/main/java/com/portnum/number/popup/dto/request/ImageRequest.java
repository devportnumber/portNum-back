package com.portnum.number.popup.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class ImageRequest {

    @NotEmpty
    private String imgUrl;
}
