package com.portnum.number.popup.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.portnum.number.popup.entity.PopupCategory;
import com.portnum.number.popup.entity.PopupStatus;
import com.portnum.number.popup.entity.embeddable.Address;
import com.portnum.number.popup.entity.embeddable.Point;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PopupModifyRequest {

    @NotNull
    private Long adminId;

    @NotNull
    private Long popupId;

    private String name;

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

    private List<String> keywords;

    private ImagesModifyRequest images;
}
