package com.portnum.number.popup.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.portnum.number.popup.entity.Popup;
import com.portnum.number.popup.entity.PopupCategory;
import com.portnum.number.popup.entity.PopupStatus;
import com.portnum.number.popup.entity.embeddable.Address;
import com.portnum.number.popup.entity.embeddable.Point;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class PopupDetailResponse implements Serializable{

    @Serial
    private static final long serialVersionUID = 1L;

    private Long popupId;

    private String name;

    private PopupCategory category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate endDate;

    private String operatingHours;

    private PopupStatus stat;

    private Point point;

    private Address address;

    private String description;

    private String detailDescription;

    private String mapUrl;

    private String representImgUrl;

    private List<ImageResponse> images;

    private List<String> keywords;

    public static PopupDetailResponse of(Popup popup){
        return PopupDetailResponse.builder()
                .popupId(popup.getId())
                .name(popup.getName())
                .category(popup.getCategory())
                .startDate(popup.getStartDate().toLocalDate())
                .endDate(popup.getEndDate().toLocalDate())
                .operatingHours(popup.getOperatingHours())
                .stat(popup.getStat())
                .point(popup.getPoint())
                .address(popup.getAddress())
                .description(popup.getDescription())
                .detailDescription(popup.getDetailDescription())
                .mapUrl(popup.getMapUrl())
                .representImgUrl(popup.getRepresentImgUrl())
                .images(popup.getImages().stream().map(ImageResponse::of).toList())
                .keywords(popup.getKeywords())
                .build();
    }
}
