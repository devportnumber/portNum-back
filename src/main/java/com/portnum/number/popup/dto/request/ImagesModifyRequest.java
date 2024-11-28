package com.portnum.number.popup.dto.request;

import com.portnum.number.popup.dto.response.ImageResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ImagesModifyRequest {

    private List<ImageRequest> addImages;
    private List<ImageResponse> updateImages;
    private List<Long> removeImages;
}

