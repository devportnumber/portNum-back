package com.portnum.number.admin.service;

import com.portnum.number.admin.domain.Admin;
import com.portnum.number.admin.dto.request.*;
import com.portnum.number.admin.dto.response.AdminInfoResponse;
import com.portnum.number.admin.repository.AdminRepository;
import com.portnum.number.fixture.AdminFixture;
import com.portnum.number.global.service.MailService;
import com.portnum.number.global.service.RedisService;
import com.portnum.number.global.exception.GlobalException;
import com.portnum.number.global.utils.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.Mockito.mockStatic;

import java.time.Duration;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminCommandServiceTest {

    @InjectMocks
    private AdminCommandService adminCommandService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailService mailService;

    @Mock
    private RedisService redisService;

    private Admin admin;
    private AdminCreateRequest createRequest;
    private AdminModifyPasswordRequest modifyPasswordRequest;

    @BeforeEach
    void setUp(){
        admin = AdminFixture.createAdmin();
        createRequest = AdminCreateRequest.builder()
                .loginId("test")
                .email("test@naver.com")
                .profileUrl("https://test.com")
                .password("test")
                .nickName("test")
                .name("test")
                .build();

        modifyPasswordRequest = AdminModifyPasswordRequest.builder()
                .adminId(1L)
                .oldPassword("oldPassword")
                .newPassword("newPassword")
                .build();
    }

    @Test
    public void create_테스트() throws Exception{
        //given
        given(adminRepository
                .existsByEmailAndLoginIdAndNickName(anyString(), anyString(), anyString()))
                .willReturn(false);
        given(adminRepository.save(any(Admin.class))).willReturn(admin);
        MockedStatic<RandomUtils> mockedStatic = mockStatic(RandomUtils.class);
        mockedStatic.when(RandomUtils::generateAlphaNumericRandomCode).thenReturn("test");

        //when
        AdminInfoResponse response = adminCommandService.create(createRequest);

        //then
        assertNotNull(response);
        assertEquals(1L, response.getAdminId());
        assertEquals(admin.getName(), response.getName());
        assertEquals(admin.getNickName(), response.getNickName());
        assertEquals("test", response.getUrlName());
        assertEquals(admin.getEmail(), response.getEmail());
        assertEquals(admin.getProfileUrl(), response.getProfileUrl());
        assertEquals("test", response.getUrlName());
        verify(passwordEncoder, times(1)).encode(anyString());

        mockedStatic.close();
    }


    @Test
    public void modify_테스트() throws Exception{
        //given
        given(adminRepository.findById(1L)).willReturn(Optional.of(admin));
        AdminModifyRequest request = AdminModifyRequest.builder()
                .adminId(1L)
                .name("test2")
                .profileUrl("https://test2.com")
                .build();

        //when
        AdminInfoResponse response = adminCommandService.modify(request);

        //then
        assertNotNull(response);
        assertEquals(1L, response.getAdminId());
        assertEquals(admin.getName(), response.getName());
        assertEquals(admin.getProfileUrl(), response.getProfileUrl());
    }

    @Test
    public void 존재하지_않는_어드민_테스트() throws Exception{
        //given
        given(adminRepository.findById(9999L)).willReturn(Optional.empty());
        AdminModifyRequest request = AdminModifyRequest.builder()
                .adminId(9999L)
                .name("test2")
                .profileUrl("https://test2.com")
                .build();

        //when
        Exception exception = assertThrows(GlobalException.class,
                () -> adminCommandService.modify(request));

        //then
        assertEquals("Not Found Admin", exception.getMessage());
    }

    @Test
    void modifyPassword_기존_비밀번호_있는경우_테스트() {
        // given
        Admin admin = mock(Admin.class);
        given(adminRepository.findById(1L)).willReturn(Optional.of(admin));
        given(admin.getPassword()).willReturn("encodedOldPassword");
        given(passwordEncoder.matches("oldPassword", "encodedOldPassword")).willReturn(true);
        given(passwordEncoder.encode("newPassword")).willReturn("encodedNewPassword");

        // when
        boolean result = adminCommandService.modifyPassword(modifyPasswordRequest, "testAccessToken");

        // then
        assertTrue(result);
        verify(admin).modifyPassword("encodedNewPassword");
        verify(redisService).deleteValues(admin.getLoginId());
        verify(redisService).setValues(eq("testAccessToken"), eq("logout"), any(Duration.class));
    }

    @Test
    void modifyPassword_기존_비밀번호_없음_테스트() {
        // given
        Admin admin = mock(Admin.class);
        modifyPasswordRequest.setOldPassword("wrongOldPassword");

        given(adminRepository.findById(1L)).willReturn(Optional.of(admin));
        given(admin.getPassword()).willReturn("encodedOldPassword");
        given(passwordEncoder.matches("wrongOldPassword", "encodedOldPassword")).willReturn(false);
        given(passwordEncoder.encode("newPassword")).willReturn("encodedNewPassword");

        // when
        boolean result = adminCommandService.modifyPassword(modifyPasswordRequest, "testAccessToken");

        // then
        assertTrue(result);
        verify(admin).modifyPassword("encodedNewPassword");
        verify(redisService).deleteValues(admin.getLoginId());
        verify(redisService).setValues(eq("testAccessToken"), eq("logout"), any(Duration.class));
    }

    @Test
    void modifyPassword_새로운_비밀번호_없는경우_테스트() {
        // given
        Admin admin = mock(Admin.class);
        modifyPasswordRequest.setNewPassword(null);

        given(adminRepository.findById(1L)).willReturn(Optional.of(admin));

        // when
        boolean result = adminCommandService.modifyPassword(modifyPasswordRequest, "testAccessToken");

        // then
        assertFalse(result);
        verify(admin, never()).modifyPassword(anyString());
        verify(redisService, never()).deleteValues(anyString());
        verify(redisService, never()).setValues(anyString(), anyString(), any(Duration.class));
    }

    @Test
    void lostEmail_성공_테스트(){
        //given
        LostRequest request = LostRequest.builder()
                .email("email")
                .nickName("nickName")
                .build();

        given(adminRepository.existsByEmailWithNickName(request.getEmail(), request.getNickName())).willReturn(true);

        //when
        boolean result = adminCommandService.lostEmail(request);

        //then
        assertTrue(result);
        verify(mailService, times(1)).sendEmail(request.getEmail(), request.getEmail());

    }

    @Test
    void lostEmail_실패_테스트(){
        //given
        LostRequest request = LostRequest.builder()
                .email("email")
                .nickName("nickName")
                .build();

        given(adminRepository.existsByEmailWithNickName(request.getEmail(), request.getNickName())).willReturn(false);

        //when
        boolean result = adminCommandService.lostEmail(request);

        //then
        assertFalse(result);
        verify(mailService, never()).sendEmail(anyString(), anyString());

    }

    @Test
    public void lostLoginId_존재하는_경우_테스트() throws Exception{
        //given
        LostLoginIdRequest request = LostLoginIdRequest.builder()
                .email("test@example.com")
                .name("AdminName")
                .build();

        Admin admin = Admin.builder()
                .loginId("admin123")
                .build();

        given(adminRepository.findByEmailWithName(request.getEmail(), request.getName())).willReturn(Optional.of(admin));

        //when
        String loginId = adminCommandService.lostLoginId(request);

        //then
        assertEquals("admin123", loginId);
    }

    @Test
    public void lostLoginId_존재하지_않는경우_테스트() throws Exception{
        //given
        LostLoginIdRequest request = LostLoginIdRequest.builder()
                .email("test@example.com")
                .name("AdminName")
                .build();

        given(adminRepository.findByEmailWithName(request.getEmail(), request.getName())).willReturn(Optional.empty());

        //when & then
        assertThrows(GlobalException.class,
                () -> adminCommandService.lostLoginId(request));
    }

    @Test
    public void lostPassword_존재하는_이메일_로그인ID_테스트() throws Exception{
        //given
        LostPasswordRequest request = LostPasswordRequest.builder()
                .email("test@example.com")
                .loginId("admin123")
                .build();

        Admin admin = mock(Admin.class);
        MockedStatic<RandomUtils> randomUtils = mockStatic(RandomUtils.class);

        String randomPassword = "randomPassword";
        String encodedPassword = "encodedPassword";

        given(adminRepository.findByEmailWithLoginId(request.getEmail(), request.getLoginId())).willReturn(Optional.of(admin));
        randomUtils.when(RandomUtils::generateRandomCode).thenReturn(randomPassword);
        given(passwordEncoder.encode(randomPassword)).willReturn(encodedPassword);

        //when
        boolean result = adminCommandService.lostPassword(request);

        //then
        assertTrue(result);
        verify(admin).modifyPassword(encodedPassword);
        verify(admin).modifyIsRqPwChange();
        verify(mailService).sendEmail(request.getEmail(), randomPassword);

        randomUtils.close();
    }

    @Test
    public void lostPassword_존재하지_않는_이메일과_로그인ID_테스트() throws Exception{
        //given
        LostPasswordRequest request = LostPasswordRequest.builder()
                .email("test@example.com")
                .loginId("admin123")
                .build();

        given(adminRepository
                .findByEmailWithLoginId(request.getEmail(), request.getLoginId()))
                .willReturn(Optional.empty());

        //when
        boolean result = adminCommandService.lostPassword(request);

        //then
        assertFalse(result);
        verify(mailService, never()).sendEmail(anyString(), anyString());
    }

}