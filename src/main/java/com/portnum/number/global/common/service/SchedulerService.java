package com.portnum.number.global.common.service;

import com.portnum.number.global.common.domain.enums.Valid;
import com.portnum.number.global.common.utils.DateUtils;
import com.portnum.number.popup.entity.Popup;
import com.portnum.number.popup.entity.PopupStatus;
import com.portnum.number.popup.repository.PopupRepository;
import com.portnum.number.store.domain.Store;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final PopupRepository popupRepository;

    @Async
    @Scheduled(cron = "0 0 0 * * ?", zone = "Asia/Seoul")
    @Transactional
    public void execute(){
        log.info("팝업 유효여부 수정 처리 작업 시작");
        List<Popup> popups = popupRepository.findAll();

        popups.forEach(popup -> {
            LocalDate startDate = LocalDate.from(popup.getStartDate());
            LocalDate endDate = LocalDate.from(popup.getEndDate());

            if (DateUtils.isBetween(startDate, endDate, LocalDate.now())) {
                popup.modifyPopupStatus(PopupStatus.Y);
            } else {
                popup.modifyPopupStatus(PopupStatus.N);
            }
        });

        log.info("팝업 유효여부 수정 처리 작업 종료");
    }
}
