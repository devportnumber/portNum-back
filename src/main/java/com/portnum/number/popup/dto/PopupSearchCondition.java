package com.portnum.number.popup.dto;

import com.portnum.number.popup.entity.PopupCategory;
import com.portnum.number.popup.entity.PopupStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PopupSearchCondition {

    private String name = null;
    private PopupCategory category = null;
    private LocalDateTime startDate = null;
    private LocalDateTime endDate = null;
    private PopupStatus stat = null;
}
