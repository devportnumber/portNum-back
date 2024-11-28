package com.portnum.number.popup.repository;

import com.portnum.number.admin.domain.Admin;
import com.portnum.number.admin.domain.RoleType;
import com.portnum.number.admin.repository.AdminRepository;
import com.portnum.number.fixture.AdminFixture;
import com.portnum.number.global.config.JpaAuditingConfig;
import com.portnum.number.global.config.QueryDSLConfig;
import com.portnum.number.popup.domain.Popup;
import com.portnum.number.popup.domain.PopupCategory;
import com.portnum.number.popup.domain.PopupStatus;
import com.portnum.number.popup.domain.embeddable.Address;
import com.portnum.number.popup.domain.embeddable.Point;
import com.portnum.number.popup.dto.PopupSearchCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({JpaAuditingConfig.class, QueryDSLConfig.class})
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class PopupRepositoryTest {

    @Autowired
    private PopupRepository popupRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Popup popup1;
    private Popup popup2;
    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = Admin.builder()
                .loginId("test")
                .email("test@naver.com")
                .profileUrl("https://test.com")
                .password("test")
                .nickName("test")
                .urlName("test")
                .name("test")
                .deleted(false)
                .isRqPwChange(false)
                .roleType(RoleType.PORT)
                .build();

        adminRepository.saveAndFlush(admin);

        popup1 = Popup.builder()
                .name("팝업 1")
                .category(PopupCategory.GOODS)
                .startDate(LocalDateTime.now().minusDays(1))
                .endDate(LocalDateTime.now().plusDays(2))
                .operatingHours("09:00~18:00")
                .stat(PopupStatus.Y)
                .admin(admin)
                .deleted(false)
                .build();

        // 두 번째 Popup
        popup2 = Popup.builder()
                .name("팝업 2")
                .category(PopupCategory.FASHION)
                .startDate(LocalDateTime.now().minusDays(2))
                .endDate(LocalDateTime.now().plusDays(1))
                .operatingHours("10:00~20:00")
                .stat(PopupStatus.N)
                .admin(admin)
                .deleted(false)
                .build();

        // Save the popup and flush to ensure it's available in tests
        popupRepository.saveAllAndFlush(List.of(popup1, popup2));
    }

    @Test
    void save_테스트() {
        //given
        Popup newPopup = Popup.builder()
                .name("새 팝업")
                .category(PopupCategory.GOODS)
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
                .admin(admin)
                .images(new ArrayList<>())
                .deleted(false)
                .build();

        //when
        Popup savedPopup = popupRepository.save(newPopup);

        //then
        assertNotNull(savedPopup);
        assertNotNull(savedPopup.getId());
        assertEquals("새 팝업", savedPopup.getName());
        assertFalse(savedPopup.getDeleted());
        assertEquals(admin.getId(), savedPopup.getAdmin().getId());
    }

    @Test
    void findById_테스트() {
        //when
        Optional<Popup> foundPopup = popupRepository.findById(popup1.getId());

        //then
        assertTrue(foundPopup.isPresent());
        assertEquals(popup1.getId(), foundPopup.get().getId());
        assertEquals(popup1.getName(), foundPopup.get().getName());
    }

    @Test
    void deletePopups_테스트() {
        //given
        List<Long> popupIds = List.of(popup1.getId());
        System.out.println(popupIds);

        //when
        popupRepository.deletePopups(popupIds);
        entityManager.flush();

        //then
        Optional<Popup> deletedPopup = popupRepository.findById(popup1.getId());
        assertFalse(deletedPopup.isPresent());
    }

    @Test
    void getPopupDetail_테스트() {
        //when
        Optional<Popup> popupDetail = popupRepository.getPopupDetail(popup1.getId());

        //then
        assertTrue(popupDetail.isPresent());
        assertEquals(popup1.getId(), popupDetail.get().getId());
        assertEquals(popup1.getName(), popupDetail.get().getName());
    }

    @Test
    void findPopups_테스트(){
        //given
        Pageable pageable = PageRequest.of(0, 10);
        PopupSearchCondition searchCondition = PopupSearchCondition.builder()
                .category(PopupCategory.GOODS)
                .stat(PopupStatus.Y)
                .build();

        //when
        Page<Popup> result = popupRepository.findPopups(popup1.getAdmin().getId(), pageable, searchCondition);

        //then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(popup1.getName(), result.getContent().get(0).getName());
    }

    @Test
    void findAllPopup_테스트(){
        //given
        Pageable pageable = PageRequest.of(0, 10);
        PopupSearchCondition searchCondition = PopupSearchCondition.builder()
                .startDate(LocalDateTime.now().minusDays(3))
                .endDate(LocalDateTime.now().plusDays(3))
                .build();

        //when
        Page<Popup> result = popupRepository.findAllPopup(pageable, searchCondition);

        //then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertThat(result.getContent()).contains(popup1, popup2);
    }
}
