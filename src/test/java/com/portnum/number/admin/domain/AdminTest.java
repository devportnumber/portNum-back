package com.portnum.number.admin.domain;

import com.portnum.number.admin.dto.request.AdminCreateRequest;
import com.portnum.number.admin.dto.request.AdminModifyRequest;
import com.portnum.number.fixture.AdminFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    @Test
    @DisplayName("어드민 생성 테스트")
    public void 어드민_생성_테스트() throws Exception{
        //when
        Admin admin = AdminFixture.createAdmin();

        //then
        assertNotNull(admin);
        assertEquals(1L, admin.getId());
        assertEquals("test@naver.com", admin.getEmail());
        assertEquals("test", admin.getNickName());
        assertEquals("test", admin.getName());
        assertEquals(RoleType.PORT, admin.getRoleType());
        assertEquals("test", admin.getLoginId());
    }

    @Test
    @DisplayName("어드민 이름 및 프로필 수정 테스트")
    public void 어드민_이름_및_프로필_수정_테스트() throws Exception{
        //given
        Admin admin = AdminFixture.createAdmin();

        String name = "test2";
        String profileUrl = "https://test2.com";

        //when
        admin.modifyNameAndProfile(name, profileUrl);

        //then
        assertEquals(name, admin.getName());
        assertEquals(profileUrl, admin.getProfileUrl());
    }

    @Test
    @DisplayName("어드민 패스워드 변경 테스트")
    public void 어드민_패스워드_변경_테스트() throws Exception{
        //given
        Admin admin = AdminFixture.createAdmin();

        //when
        admin.modifyPassword("test2");

        //then
        assertNotEquals("test", admin.getPassword());
        assertEquals("test2", admin.getPassword());
        assertEquals(Boolean.FALSE, admin.getIsRqPwChange());
    }

    @Test
    @DisplayName("어드민 패스워드 변경 필요 테스트")
    public void 어드민_패스워드_변경_필요_테스트() throws Exception{
        //given
        Admin admin = AdminFixture.createAdmin();

        //when
        admin.modifyIsRqPwChange();

        //then
        assertEquals(Boolean.TRUE, admin.getIsRqPwChange());
    }
}