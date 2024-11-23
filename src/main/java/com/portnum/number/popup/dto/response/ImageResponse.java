package com.portnum.number.popup.dto.response;


import com.portnum.number.popup.domain.Image;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageResponse {

    private Long imgId;
    private String imgUrl;

    public static ImageResponse of(Image image){
        return ImageResponse.builder()
                .imgId(image.getId())
                .imgUrl(image.getImgUrl())
                .build();
    }
}
