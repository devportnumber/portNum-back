package com.portnum.number.popup.dto;

import com.portnum.number.popup.domain.PopupCategory;
import com.portnum.number.popup.domain.PopupStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PopupSearchCondition {

    private String name;
    private PopupCategory category;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private PopupStatus stat;
}
