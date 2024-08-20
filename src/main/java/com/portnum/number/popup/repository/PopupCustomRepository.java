package com.portnum.number.popup.repository;

import com.portnum.number.popup.dto.PopupSearchCondition;
import com.portnum.number.popup.entity.Popup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PopupCustomRepository {

    Page<Popup> findPopups(Long adminId, Pageable pageable, PopupSearchCondition searchCondition);
}
