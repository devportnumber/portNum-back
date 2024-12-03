package com.portnum.number.popup.dto.request;

import com.portnum.number.popup.dto.response.ImageResponse;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ImagesModifyRequest {

    private List<ImageRequest> addImages;
    private List<ImageResponse> updateImages;
    private List<Long> removeImages;
}

