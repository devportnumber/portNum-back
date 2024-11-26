package com.portnum.number.fixture;

import com.portnum.number.admin.domain.Admin;
import com.portnum.number.popup.domain.Popup;
import com.portnum.number.popup.domain.PopupCategory;
import com.portnum.number.popup.domain.PopupStatus;
import com.portnum.number.popup.domain.embeddable.Address;
import com.portnum.number.popup.domain.embeddable.Point;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PopupFixture {

    public static Popup createPopup(){
        Popup popup = Popup.builder()
                .id(1L)
                .name("팝업")
                .category(PopupCategory.GOODS)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .operatingHours("영업시간")
                .stat(PopupStatus.Y)
                .point(new Point(1.0F, 1.0F))
                .address(new Address("서울특별시", "강남구", "강남대로"))
                .description("설명")
                .detailDescription("상세설명")
                .mapUrl("mapUrl")
                .representImgUrl("representImgUrl")
                .keywords(new ArrayList<>())
                .admin(AdminFixture.createAdmin())
                .images(new ArrayList<>())
                .deleted(false)
                .build();

        popup.modifyCreatedDt(LocalDateTime.now());
        popup.modifyUpdatedDt(LocalDateTime.now());

        return popup;
    }

    public static Popup createEmptyImagePopup(Admin admin){
        Popup popup = Popup.builder()
                .id(1L)
                .name("팝업")
                .category(PopupCategory.GOODS)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .operatingHours("영업시간")
                .stat(PopupStatus.Y)
                .point(new Point(1.0F, 1.0F))
                .address(new Address("서울특별시", "강남구", "강남대로"))
                .description("설명")
                .detailDescription("상세설명")
                .mapUrl("mapUrl")
                .representImgUrl("representImgUrl")
                .keywords(new ArrayList<>())
                .admin(admin)
                .images(new ArrayList<>())
                .deleted(false)
                .build();

        popup.modifyCreatedDt(LocalDateTime.now());
        popup.modifyUpdatedDt(LocalDateTime.now());

        return popup;
    }

    public static Popup createExistsImagePopup(Admin admin){
        Popup popup = Popup.builder()
                .id(1L)
                .name("팝업")
                .category(PopupCategory.GOODS)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .operatingHours("영업시간")
                .stat(PopupStatus.Y)
                .point(new Point(1.0F, 1.0F))
                .address(new Address("서울특별시", "강남구", "강남대로"))
                .description("설명")
                .detailDescription("상세설명")
                .mapUrl("mapUrl")
                .representImgUrl("representImgUrl")
                .keywords(new ArrayList<>())
                .admin(admin)
                .images(List.of(ImageFixture.createImageForSuccess(1L, 1L)))
                .deleted(false)
                .build();

        popup.modifyCreatedDt(LocalDateTime.now());
        popup.modifyUpdatedDt(LocalDateTime.now());

        return popup;
    }
}
