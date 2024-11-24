package com.portnum.number.admin.service;

import com.portnum.number.admin.repository.AdminRepository;
import com.portnum.number.global.exception.Code;
import com.portnum.number.global.exception.GlobalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminQueryServiceTest {

    @InjectMocks
    private AdminQueryService adminQueryService;

    @Mock
    private AdminRepository adminRepository;

    private String email;
    private String nickName;
    private String loginId;

    @BeforeEach
    void setUp(){
        email = "test@example.com";
        nickName = "test";
        loginId = "test";
    }

    @Test
    public void validateEmail_존재_테스트() throws Exception{
        //given
        given(adminRepository.existsByEmail(email)).willReturn(true);

        //when
        boolean result = adminQueryService.validateEmail(email);

        //then
        assertTrue(result);
        verify(adminRepository, times(1)).existsByEmail(email);
    }

    @Test
    public void validateEmail_미존재_테스트() throws Exception{
        //given
        given(adminRepository.existsByEmail("notexist" + email)).willReturn(false);

        //when
        boolean result = adminQueryService.validateEmail("notexist" + email);

        //then
        assertFalse(result);
        verify(adminRepository, times(1)).existsByEmail("notexist" + email);
    }

    @Test
    public void validateNickName_존재_테스트() throws Exception{
        //given
        given(adminRepository.existsByNickName(nickName)).willReturn(true);

        //when
        boolean result = adminQueryService.validateNickName(nickName);

        //then
        assertTrue(result);
        verify(adminRepository, times(1)).existsByNickName(nickName);
    }

    @Test
    public void validateNickName_미존재_테스트() throws Exception{
        //given
        given(adminRepository.existsByNickName("notexist" + nickName)).willReturn(false);

        //when
        boolean result = adminQueryService.validateNickName("notexist" + nickName);

        //then
        assertFalse(result);
        verify(adminRepository, times(1)).existsByNickName("notexist" + nickName);
    }

    @Test
    public void validateLoginId_존재_테스트() throws Exception{
        //given
        given(adminRepository.existsByLoginId(loginId)).willReturn(true);

        //when
        boolean result = adminQueryService.validateLoginId(loginId);

        //then
        assertTrue(result);
        verify(adminRepository, times(1)).existsByLoginId(loginId);
    }

    @Test
    public void validateLoginId_미존재_테스트() throws Exception{
        //given
        given(adminRepository.existsByLoginId("notexist" + loginId)).willReturn(false);

        //when
        boolean result = adminQueryService.validateLoginId("notexist" + loginId);

        //then
        assertFalse(result);
        verify(adminRepository, times(1)).existsByLoginId("notexist" + loginId);
    }

    @Test
    public void getNickName_존재_테스트() throws Exception {
        // given
        String urlName = "testUrl";
        String expectedNickName = "testNick";
        given(adminRepository.findNickNameByUrlName(urlName)).willReturn(Optional.of(expectedNickName));

        // when
        String result = adminQueryService.getNickName(urlName);

        // then
        assertEquals(expectedNickName, result);
        verify(adminRepository, times(1)).findNickNameByUrlName(urlName);
    }

    @Test
    public void getNickName_미존재_테스트() throws Exception {
        // given
        String urlName = "notExistUrl";
        given(adminRepository.findNickNameByUrlName(urlName)).willReturn(Optional.empty());

        // when & then
        GlobalException exception = assertThrows(GlobalException.class, () -> adminQueryService.getNickName(urlName));
        assertEquals(Code.NOT_FOUND, exception.getErrorCode());
        assertEquals("Not Found Admin", exception.getMessage());
        verify(adminRepository, times(1)).findNickNameByUrlName(urlName);
    }


}