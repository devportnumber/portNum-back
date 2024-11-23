package com.portnum.number.admin.domain;

import com.portnum.number.admin.dto.request.AdminCreateRequest;
import com.portnum.number.admin.dto.request.AdminModifyRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    private AdminCreateRequest createRequest;

    @BeforeEach
    void setUp(){
        createRequest = AdminCreateRequest.builder()
                .loginId("test")
                .email("test@naver.com")
                .profileUrl("https://test.com")
                .password("test")
                .nickName("test")
                .name("test")
                .build();
    }

    @Test
    @DisplayName("어드민 생성 테스트")
    public void 어드민_생성_테스트() throws Exception{
        //given
        AdminCreateRequest adminCreateRequest = createRequest;

        //when
        Admin admin = Admin.of(adminCreateRequest, "test");

        //then
        assertEquals(adminCreateRequest.getEmail(), admin.getEmail());
        assertEquals(adminCreateRequest.getNickName(), admin.getNickName());
        assertEquals(adminCreateRequest.getName(), admin.getName());
        assertEquals(adminCreateRequest.getProfileUrl(), admin.getProfileUrl());
        assertEquals(RoleType.PORT, admin.getRoleType());
        assertEquals(adminCreateRequest.getPassword(), admin.getPassword());
        assertEquals(adminCreateRequest.getLoginId(), admin.getLoginId());
        assertEquals("test", admin.getUrlName());
    }

    @Test
    @DisplayName("어드민 이름 및 프로필 수정 테스트")
    public void 어드민_이름_및_프로필_수정_테스트() throws Exception{
        //given
        Admin admin = Admin.of(createRequest, "test");
        AdminModifyRequest adminModifyRequest = AdminModifyRequest.builder()
                .name("test2")
                .profileUrl("https://test2.com")
                .build();

        //when
        admin.modifyAdmin(adminModifyRequest);

        //then
        assertEquals(adminModifyRequest.getName(), admin.getName());
        assertEquals(adminModifyRequest.getProfileUrl(), admin.getProfileUrl());
    }

    @Test
    @DisplayName("어드민 패스워드 변경 테스트")
    public void 어드민_패스워드_변경_테스트() throws Exception{
        //given
        Admin admin = Admin.of(createRequest, "test");

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
        Admin admin = Admin.of(createRequest, "test");

        //when
        admin.modifyIsRqPwChange();

        //then
        assertEquals(Boolean.TRUE, admin.getIsRqPwChange());
    }
}