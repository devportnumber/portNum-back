package com.portnum.number.popup.dto.response;

import com.portnum.number.popup.entity.Popup;
import com.portnum.number.popup.entity.PopupCategory;
import com.portnum.number.popup.entity.PopupStatus;
import com.portnum.number.popup.entity.embeddable.Address;
import com.portnum.number.popup.entity.embeddable.Point;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PopupDetailResponse{

    private Long popupId;

    private String name;

    private PopupCategory category;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private PopupStatus stat;

    private Point point;

    private Address address;

    private String description;

    private String mapUrl;

    private String representImgUrl;

    private List<ImageResponse> images;

    public static PopupDetailResponse of(Popup popup){
        return PopupDetailResponse.builder()
                .popupId(popup.getId())
                .name(popup.getName())
                .category(popup.getCategory())
                .startDate(popup.getStartDate())
                .endDate(popup.getEndDate())
                .stat(popup.getStat())
                .point(popup.getPoint())
                .address(popup.getAddress())
                .description(popup.getDescription())
                .mapUrl(popup.getMapUrl())
                .representImgUrl(popup.getRepresentImgUrl())
                .images(popup.getImages().stream().map(ImageResponse::of).toList())
                .build();
    }
}
