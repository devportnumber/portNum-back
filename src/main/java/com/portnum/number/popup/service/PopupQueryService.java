package com.portnum.number.popup.service;

import com.portnum.number.admin.entity.Admin;
import com.portnum.number.admin.repository.AdminRepository;
import com.portnum.number.global.common.dto.response.PageResponseDto;
import com.portnum.number.global.exception.Code;
import com.portnum.number.global.exception.GlobalException;
import com.portnum.number.popup.dto.PopupSearchCondition;
import com.portnum.number.popup.dto.response.PopupDetailResponse;
import com.portnum.number.popup.dto.response.PopupInfoResponse;
import com.portnum.number.popup.entity.Popup;
import com.portnum.number.popup.repository.PopupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
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

    public PopupDetailResponse readPopupDetail(Long adminId, Long popupId) {
        validateAdmin(adminId);

        Popup findPopup = popupRepository.getPopupDetail(popupId)
                .orElseThrow(() -> new GlobalException(Code.NOT_FOUND, "Not Found Popup"));

        return PopupDetailResponse.of(findPopup);
    }

    public PageResponseDto read(String nickName, int pageNo, PopupSearchCondition searchCondition) {
        int page = pageNo == 0 ? 0 : pageNo - 1;
        int pageLimit = 10;

        Pageable pageable = PageRequest.of(page, pageLimit);

        Admin admin = validateAdminWithNickName(nickName);

        Page<Popup> popups = popupRepository.findPopups(admin.getId(), pageable, searchCondition);

        return PageResponseDto.of(popups, PopupInfoResponse :: of);
    }

    @Cacheable(value = "popupDetail", key = "#popupId", cacheManager = "popupCacheManager")
    public PopupDetailResponse readPopupDetail(String nickName, Long popupId) {
        validateAdminWithNickName(nickName);

        Popup findPopup = popupRepository.getPopupDetail(popupId)
                .orElseThrow(() -> new GlobalException(Code.NOT_FOUND, "Not Found Popup"));

        return PopupDetailResponse.of(findPopup);
    }

    public PageResponseDto portNumAdminRead(int pageNo, PopupSearchCondition searchCondition) {
        int page = pageNo == 0 ? 0 : pageNo - 1;
        int pageLimit = 10;

        Pageable pageable = PageRequest.of(page, pageLimit);

        Page<Popup> popups = popupRepository.findAllPopup(pageable, searchCondition);

        return PageResponseDto.of(popups, PopupInfoResponse :: of);
    }

    private void validateAdmin(Long adminId) {
        if(!adminRepository.existsById(adminId))
            throw new GlobalException(Code.NOT_FOUND, "Not Found Admin");
    }

    private Admin validateAdminWithNickName(String nickName) {
        return adminRepository.findByNickName(nickName)
                .orElseThrow(() -> new GlobalException(Code.NOT_FOUND, "Not Found Admin"));
    }
}
