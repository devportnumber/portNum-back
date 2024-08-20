package com.portnum.number.popup.dto.response;

import com.portnum.number.popup.entity.Popup;
import com.portnum.number.popup.entity.PopupCategory;
import com.portnum.number.popup.entity.PopupStatus;
import com.portnum.number.popup.entity.embeddable.Address;
import com.portnum.number.popup.entity.embeddable.Point;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PopupInfoResponse {

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

    public static PopupInfoResponse of(Popup popup){
        return PopupInfoResponse.builder()
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
                .build();
    }
}
