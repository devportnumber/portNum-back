package com.portnum.number.popup.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.portnum.number.popup.domain.PopupCategory;
import com.portnum.number.popup.domain.PopupStatus;
import com.portnum.number.popup.domain.embeddable.Address;
import com.portnum.number.popup.domain.embeddable.Point;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PopupCreateRequest {

    @NotNull
    private Long adminId;

    @NotEmpty
    private String name;

    @NotNull
    private PopupCategory category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime endDate;

    private String operatingHours;

    private PopupStatus stat;

    private Point point;

    private Address address;

    private String description;

    private String detailDescription;

    private String mapUrl;

    private String representImgUrl;

    private List<ImageRequest> images;

    private List<String> keywords;
}
