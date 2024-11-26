package com.portnum.number.popup.service;


import com.portnum.number.admin.domain.Admin;
import com.portnum.number.admin.repository.AdminRepository;
import com.portnum.number.fixture.ImageFixture;
import com.portnum.number.fixture.PopupFixture;
import com.portnum.number.global.common.service.ImageUploadService;
import com.portnum.number.global.exception.GlobalException;
import com.portnum.number.popup.domain.Image;
import com.portnum.number.popup.domain.Popup;
import com.portnum.number.popup.domain.PopupCategory;
import com.portnum.number.popup.domain.PopupStatus;
import com.portnum.number.popup.domain.embeddable.Address;
import com.portnum.number.popup.domain.embeddable.Point;
import com.portnum.number.popup.dto.request.ImagesModifyRequest;
import com.portnum.number.popup.dto.request.PopupCreateRequest;
import com.portnum.number.popup.dto.request.PopupModifyRequest;
import com.portnum.number.popup.dto.request.PopupRemoveRequest;
import com.portnum.number.popup.dto.response.ImageResponse;
import com.portnum.number.popup.dto.response.PopupDetailResponse;
import com.portnum.number.popup.repository.ImageRepository;
import com.portnum.number.popup.repository.PopupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PopupCommandServiceTest {

    @InjectMocks
    private PopupCommandService popupCommandService;

    @Mock
    private PopupRepository popupRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private ImageUploadService imageUploadService;

    private PopupCreateRequest createRequest;
    private PopupModifyRequest modifyEmptyImageRequest;
    private PopupModifyRequest modifyExistsImageRequest;
    private PopupRemoveRequest removeRequest;
    private Admin admin;
    private Popup existsImagePopup;
    private Popup emptyImagePopup;
    private Image image1;
    private Image image2;


    @BeforeEach
    void setUp() {
        admin = Admin.builder()
                .id(1L)
                .build();

        emptyImagePopup = PopupFixture.createEmptyImagePopup(admin);
        existsImagePopup = PopupFixture.createExistsImagePopup(admin);

        image1 = ImageFixture.createImage(1L, existsImagePopup);
        image2 = ImageFixture.createImageForFail(3L);

        ImagesModifyRequest imagesModifyRequest = ImagesModifyRequest.builder()
                .updateImages(List.of(
                        ImageResponse.from(image1),
                        ImageResponse.from(image2))
                )
                .build();

        createRequest = PopupCreateRequest.builder()
                .adminId(1L)
                .name("새 팝업")
                .category(PopupCategory.FASHION)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2))
                .operatingHours("새 영업시간")
                .stat(PopupStatus.Y)
                .point(new Point(2.0F, 2.0F))
                .address(new Address("서울특별시", "종로구", "종로대로"))
                .description("새 설명")
                .detailDescription("새 상세설명")
                .mapUrl("새 mapUrl")
                .representImgUrl("새 representImgUrl")
                .keywords(new ArrayList<>())
                .build();


        modifyEmptyImageRequest = PopupModifyRequest.builder()
                .adminId(1L)
                .popupId(1L)
                .name("새 팝업")
                .category(PopupCategory.GOODS)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2))
                .operatingHours("새 영업시간")
                .stat(PopupStatus.Y)
                .build();

        modifyExistsImageRequest = PopupModifyRequest.builder()
                .adminId(1L)
                .popupId(1L)
                .name("새 팝업")
                .category(PopupCategory.GOODS)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2))
                .operatingHours("새 영업시간")
                .stat(PopupStatus.Y)
                .images(imagesModifyRequest)
                .build();

        removeRequest = PopupRemoveRequest.builder()
                .popupIds(List.of(1L))
                .adminId(1L)
                .build();

    }

    @Test
    void create_테스트(){
        //given
        given(adminRepository.findById(1L)).willReturn(Optional.of(admin));
        given(popupRepository.save(any(Popup.class))).willReturn(emptyImagePopup);

        //when
        PopupDetailResponse response = popupCommandService.create(createRequest);

        //then
        assertNotNull(response);
        assertEquals(1L, response.getPopupId());
        assertEquals(emptyImagePopup.getName(), response.getName());
        verify(popupRepository).save(any(Popup.class));
    }

    @Test
    @DisplayName("이미지 변경이 없는 경우의 수정 테스트")
    void modify_변경_이미지없음_테스트() {
        // given
        given(popupRepository.findById(1L)).willReturn(Optional.of(emptyImagePopup));

        // when
        PopupDetailResponse response = popupCommandService.modify(modifyEmptyImageRequest, "test");

        // then
        assertNotNull(response);
        assertEquals(emptyImagePopup.getId(), response.getPopupId());
        assertEquals(modifyEmptyImageRequest.getName(), emptyImagePopup.getName());
    }

    @Test
    @DisplayName("이미지 변경이 있는 경우의 수정 테스트, 만약 해당 팝업에 소속된 이미지가 아니면 실패")
    void modify_변경_이미지있음_실패_테스트(){
        //given
        given(popupRepository.findById(1L)).willReturn(Optional.of(existsImagePopup));
        given(imageRepository.findById(1L)).willReturn(Optional.of(image1));
        given(imageRepository.findById(3L)).willReturn(Optional.of(image2));

        //when & then
        assertThrows(GlobalException.class,
                () -> popupCommandService.modify(modifyExistsImageRequest, "test")
        );
    }

    @Test
    @DisplayName("이미지 변경이 있는 경우의 수정 테스트, 만약 해당 팝업에 소속된 이미지면 성공")
    void modify_변경_이미지있음_성공_테스트(){
        //given
        given(popupRepository.findById(1L)).willReturn(Optional.of(existsImagePopup));
        given(imageRepository.findById(1L)).willReturn(Optional.of(image1));

        PopupModifyRequest modifyRequest = PopupModifyRequest.builder()
                .adminId(1L)
                .popupId(1L)
                .name("새 팝업")
                .category(PopupCategory.GOODS)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(2))
                .operatingHours("새 영업시간")
                .stat(PopupStatus.Y)
                .images(ImagesModifyRequest.builder()
                        .updateImages(List.of(
                                ImageResponse.builder().imgId(1L).imgUrl("수정테스트").build()
                        ))
                        .build())
                .build();

        //when
        PopupDetailResponse response = popupCommandService.modify(modifyRequest, "test");

        //then
        assertNotNull(response);
        assertEquals(emptyImagePopup.getId(), response.getPopupId());
        assertEquals(modifyEmptyImageRequest.getName(), existsImagePopup.getName());
        assertEquals(image1.getImgUrl(), "수정테스트");
    }

    // 팝업 이미지 추가, 삭제 테스트


    @Test
    public void remove_테스트() throws Exception{
        //given
        given(popupRepository.getPopupDetail(any(Long.class))).willReturn(Optional.of(existsImagePopup));
        given(imageRepository.findByPopupId(existsImagePopup.getId())).willReturn(List.of(image1));

        //when
        popupCommandService.remove(removeRequest, "test");
        
        //then
        verify(popupRepository, times(1)).deletePopups(removeRequest.getPopupIds());
        verify(imageRepository, times(1)).deleteAllByPopupId(existsImagePopup.getId());
        verify(imageUploadService, times(1)).deleteImages(List.of(image1.getImgUrl()));
    }

}