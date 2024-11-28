package com.portnum.number.popup.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.portnum.number.fixture.ImageFixture;
import com.portnum.number.fixture.PopupFixture;
import com.portnum.number.popup.domain.embeddable.Address;
import com.portnum.number.popup.domain.embeddable.Point;
import com.portnum.number.popup.dto.request.ImagesModifyRequest;
import com.portnum.number.popup.dto.request.PopupModifyRequest;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PopupTest {

    @Test
    public void 팝업_생성_테스트() throws Exception{
        //when
        Popup popup = PopupFixture.createPopup();

        //then
        assertNotNull(popup);
        assertEquals(1L, popup.getId());
        assertEquals(false, popup.getDeleted());
    }

    @Test
    public void 팝업_수정_테스트() throws Exception{
        //given
        Popup popup = PopupFixture.createPopup();

        PopupModifyRequest request = PopupModifyRequest.builder()
                .adminId(1L)
                .popupId(1L)
                .name("팝업수정")
                .category(PopupCategory.CAFE)
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(1))
                .operatingHours("영업시간수정")
                .point(new Point(1.0F, 1.0F))
                .address(new Address("서울특별시", "강남구", "강남대로"))
                .description("설명수정")
                .detailDescription("상세설명수정")
                .mapUrl("mapUrl")
                .representImgUrl("representImgUrl")
                .keywords(new ArrayList<>())
                .build();

        //when
        popup.modifyPopup(request);

        //then
        assertEquals("팝업수정", popup.getName());
        assertEquals(PopupCategory.CAFE, popup.getCategory());
        assertEquals("영업시간수정", popup.getOperatingHours());
        assertEquals(new Point(1.0F, 1.0F), popup.getPoint());
        assertEquals("설명수정", popup.getDescription());
        assertEquals("상세설명수정", popup.getDetailDescription());
        assertEquals("mapUrl", popup.getMapUrl());
        assertEquals("representImgUrl", popup.getRepresentImgUrl());
        assertEquals(new ArrayList<>(), popup.getKeywords());
    }

    @Test
    public void 팝업_상태_변경_테스트() throws Exception{
        //given
        Popup popup = PopupFixture.createPopup();

        //when
        popup.modifyPopupStatus(PopupStatus.E);

        //then
        assertEquals(popup.getStat(), PopupStatus.E);
    }


}