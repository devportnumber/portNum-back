package com.portnum.number.global.service;

import com.portnum.number.popup.domain.Popup;
import com.portnum.number.popup.domain.PopupStatus;
import com.portnum.number.popup.repository.PopupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceTest {

    @InjectMocks
    private SchedulerService schedulerService;

    @Mock
    private PopupRepository popupRepository;

    private Popup expiredPopup;

    @BeforeEach
    void setUp(){
        expiredPopup = Popup.builder()
                .id(1L)
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().minusDays(1))
                .stat(PopupStatus.Y)
                .build();
    }

    @Test
    @DisplayName("기간이 지난 팝업의 상태를 N으로 변경한다.")
    void execute_테스트() {
        //given
        given(popupRepository.findAll()).willReturn(List.of(expiredPopup));

        //when
        schedulerService.execute();

        //then
        assertEquals(PopupStatus.N, expiredPopup.getStat());
    }

}