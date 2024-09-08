package com.portnum.number.popup.repository;

import com.portnum.number.popup.dto.PopupSearchCondition;
import com.portnum.number.popup.entity.Popup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;

public interface PopupCustomRepository {

    Page<Popup> findPopups(Long adminId, Pageable pageable, PopupSearchCondition searchCondition);

    Page<Popup> findAllPopup(Pageable pageable, PopupSearchCondition searchCondition);
}
