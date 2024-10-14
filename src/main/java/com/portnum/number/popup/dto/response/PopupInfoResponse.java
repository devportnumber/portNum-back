package com.portnum.number.popup.dto.response;

import com.portnum.number.popup.entity.Popup;
import com.portnum.number.popup.entity.PopupCategory;
import com.portnum.number.popup.entity.PopupStatus;
import com.portnum.number.popup.entity.embeddable.Address;
import com.portnum.number.popup.entity.embeddable.Point;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PopupInfoResponse {

    private Long popupId;

    private String name;

    private PopupCategory category;

    private LocalDate startDate;

    private LocalDate endDate;

    private LocalDate regdt;

    private String operatingHours;

    private PopupStatus stat;

    private Point point;

    private Address address;

    private String description;

    private String detailDescription;

    private String mapUrl;

    private String representImgUrl;

    private List<String> keywords;

    public static PopupInfoResponse of(Popup popup){
        return PopupInfoResponse.builder()
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
                .keywords(popup.getKeywords())
                .regdt(popup.getCreatedDt().toLocalDate())
                .build();
    }
}
