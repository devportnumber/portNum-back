package com.portnum.number.popup.service;

import com.portnum.number.admin.domain.Admin;
import com.portnum.number.admin.repository.AdminRepository;
import com.portnum.number.fixture.PopupFixture;
import com.portnum.number.global.common.dto.response.PageResponseDto;
import com.portnum.number.global.exception.Code;
import com.portnum.number.global.exception.GlobalException;
import com.portnum.number.popup.domain.Popup;
import com.portnum.number.popup.dto.PopupSearchCondition;
import com.portnum.number.popup.dto.response.PopupDetailResponse;
import com.portnum.number.popup.dto.response.PopupInfoResponse;
import com.portnum.number.popup.repository.PopupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PopupQueryServiceTest {

    @InjectMocks
    private PopupQueryService popupQueryService;

    @Mock
    private PopupRepository popupRepository;

    @Mock
    private AdminRepository adminRepository;

    private Admin admin;
    private Popup popup;

    @BeforeEach
    void setUp() {
        admin = Admin.builder()
                .id(1L)
                .nickName("test")
                .build();

        popup = PopupFixture.createPopup();
    }

    @Test
    void read_테스트(){
        //given
        PopupSearchCondition searchCondition = PopupSearchCondition.builder()
                .name("Test")
                .build();

        int pageNo = 0;
        Pageable pageable = PageRequest.of(pageNo, 10);


        given(adminRepository.findByNickName("test")).willReturn(Optional.of(admin));
        given(popupRepository.findPopups(admin.getId(), pageable, searchCondition))
                .willReturn(new PageImpl<>(List.of(popup)));

        //when
        PageResponseDto<PopupInfoResponse> response = popupQueryService.read("test", pageNo, searchCondition);

        //then
        assertNotNull(response);
        assertEquals(1, response.getNumberOfElements()); // 반환된 요소 개수 검증
        assertEquals(popup.getId(), response.getData().get(0).getPopupId()); // 반환된 데이터의 ID 검증
        assertEquals(popup.getName(), response.getData().get(0).getName()); // 반환된 데이터의 이름 검증
    }

    @Test
    void readPopupDetail_테스트(){
        //given
        given(adminRepository.findByNickName("test")).willReturn(Optional.of(admin));
        given(popupRepository.getPopupDetail(popup.getId())).willReturn(Optional.of(popup));

        // when
        PopupDetailResponse response = popupQueryService.readPopupDetail(admin.getNickName(), popup.getId());

        // then
        assertNotNull(response);
        assertEquals(popup.getId(), response.getPopupId());
        assertEquals(popup.getName(), response.getName());
        verify(adminRepository).findByNickName(admin.getNickName());
        verify(popupRepository).getPopupDetail(popup.getId());
    }

    @Test
    void readPopupDetail_팝업없음_테스트() {
        // given
        Long popupId = 99L;

        given(adminRepository.findByNickName(admin.getNickName())).willReturn(Optional.of(admin));
        given(popupRepository.getPopupDetail(popupId)).willReturn(Optional.empty());

        // when & then
        GlobalException exception = assertThrows(GlobalException.class, () ->
                popupQueryService.readPopupDetail(admin.getNickName(), popupId)
        );

        assertEquals(Code.NOT_FOUND, exception.getErrorCode());
        assertEquals("Not Found Popup", exception.getMessage());
    }

    @Test
    void portNumAdminRead_테스트() {
        // given
        int pageNo = 0;
        PopupSearchCondition searchCondition = PopupSearchCondition.builder()
                .name("Test")
                .build();

        Pageable pageable = PageRequest.of(pageNo, 10);
        given(popupRepository.findAllPopup(pageable, searchCondition))
                .willReturn(new PageImpl<>(List.of(popup)));

        // when
        PageResponseDto<PopupInfoResponse> response = popupQueryService.portNumAdminRead(pageNo, searchCondition);

        // then
        assertNotNull(response);
        assertEquals(1, response.getNumberOfElements()); // 반환된 요소 개수 검증
        assertEquals(popup.getId(), response.getData().get(0).getPopupId()); // 반환된 데이터의 ID 검증
        assertEquals(popup.getName(), response.getData().get(0).getName()); // 반환된 데이터의 이름 검증
    }

    @Test
    void validateAdminWithNickName_존재하지않는_Admin_테스트() {
        // given
        String nickName = "invalidNickName";

        given(adminRepository.findByNickName(nickName)).willReturn(Optional.empty());

        // when & then
        GlobalException exception = assertThrows(GlobalException.class, () ->
                popupQueryService.read(
                        nickName,
                        1,
                        PopupSearchCondition.builder().name("Test").build()
                )
        );

        assertEquals(Code.NOT_FOUND, exception.getErrorCode());
        assertEquals("Not Found Admin", exception.getMessage());
    }
}