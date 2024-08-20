package com.portnum.number.popup.service;

import com.portnum.number.admin.entity.Admin;
import com.portnum.number.admin.repository.AdminRepository;
import com.portnum.number.global.common.dto.response.PageResponseDto;
import com.portnum.number.global.exception.Code;
import com.portnum.number.global.exception.GlobalException;
import com.portnum.number.popup.dto.PopupSearchCondition;
import com.portnum.number.popup.dto.response.PopupInfoResponse;
import com.portnum.number.popup.entity.Popup;
import com.portnum.number.popup.repository.PopupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PopupQueryService {

    private final PopupRepository popupRepository;
    private final AdminRepository adminRepository;

    public PageResponseDto read(Long adminId, int pageNo, PopupSearchCondition searchCondition) {
        int page = pageNo == 0 ? 0 : pageNo - 1;
        int pageLimit = 10;

        Pageable pageable = PageRequest.of(page, pageLimit);

        validateAdmin(adminId);

        Page<Popup> popups = popupRepository.findPopups(adminId, pageable, searchCondition);

        return PageResponseDto.of(popups, PopupInfoResponse :: of);
    }

    private void validateAdmin(Long adminId) {
        adminRepository.findById(adminId)
                .orElseThrow(() -> new GlobalException(Code.NOT_FOUND, "Not Found Admin"));
    }

}
