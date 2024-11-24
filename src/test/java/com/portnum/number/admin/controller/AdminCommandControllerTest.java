package com.portnum.number.admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portnum.number.admin.dto.request.*;
import com.portnum.number.admin.dto.response.AdminInfoResponse;
import com.portnum.number.admin.service.AdminCommandService;
import com.portnum.number.global.filter.LatencyLoggingFilter;
import com.portnum.number.global.security.jwt.JwtAuthenticationFilter;
import com.portnum.number.global.security.jwt.JwtTokenProvider;
import com.portnum.number.global.security.jwt.JwtVerificationFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AdminCommandController.class,
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {LatencyLoggingFilter.class})
    }
)
@AutoConfigureMockMvc(addFilters = false)
class AdminCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminCommandService adminCommandService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    private ObjectMapper objectMapper;


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void addAdmin_성공_테스트() throws Exception{
        //given
        AdminCreateRequest request = AdminCreateRequest.builder()
                .email("test@example.com")
                .loginId("test")
                .password("test")
                .nickName("test")
                .name("test")
                .build();

        AdminInfoResponse response = AdminInfoResponse.builder()
                .adminId(1L)
                .email("test@example.com")
                .name("test")
                .nickName("test")
                .profileUrl("https://test.com")
                .build();

        given(adminCommandService.create(any(AdminCreateRequest.class))).willReturn(response);

        //when & then
        mockMvc.perform(post("/admin/signup")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nickName").value("test"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));

        verify(adminCommandService, times(1)).create(any(AdminCreateRequest.class));
    }

    @Test
    void modifyAdmin_성공() throws Exception{
        //given
        AdminModifyRequest request = AdminModifyRequest.builder()
                .adminId(1L)
                .name("test2")
                .profileUrl("https://test2.com")
                .build();
        AdminInfoResponse response = AdminInfoResponse.builder()
                .adminId(1L)
                .email("test@example.com")
                .name("test2")
                .nickName("test")
                .profileUrl("https://test2.com")
                .build();

        given(adminCommandService.modify(any(AdminModifyRequest.class))).willReturn(response);

        //when & then
        mockMvc.perform(patch("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("test2"))
                .andExpect(jsonPath("$.data.profileUrl").value("https://test2.com"));
    }

    @Test
    void modifyPassword_성공() throws Exception {
        // given
        AdminModifyPasswordRequest request = AdminModifyPasswordRequest.builder()
                .adminId(1L)
                .oldPassword("oldPassword")
                .newPassword("newPassword")
                .build();

        given(jwtTokenProvider.resolveAccessToken(any(HttpServletRequest.class))).willReturn("validToken");
        given(adminCommandService.modifyPassword(any(AdminModifyPasswordRequest.class), eq("validToken"))).willReturn(true);

        // when & then
        mockMvc.perform(patch("/admin/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));

        verify(adminCommandService, times(1)).modifyPassword(eq(request), eq("validToken"));
    }

    @Test
    void lostEmail_성공() throws Exception {
        // given
        LostRequest request = LostRequest.builder()
                .email("test@domain.com")
                .nickName("test")
                .build();

        given(adminCommandService.lostEmail(any(LostRequest.class))).willReturn(true);

        // when & then
        mockMvc.perform(post("/admin/lost/email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));

        verify(adminCommandService, times(1)).lostEmail(any(LostRequest.class));
    }

    @Test
    void lostLoginId_성공() throws Exception {
        // given
        LostLoginIdRequest request = LostLoginIdRequest.builder()
                .email("test@domain.com")
                .name("test")
                .build();

        given(adminCommandService.lostLoginId(any(LostLoginIdRequest.class))).willReturn("adminLoginId");

        // when & then
        mockMvc.perform(post("/admin/lost/loginId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("adminLoginId"));

        verify(adminCommandService, times(1)).lostLoginId(any(LostLoginIdRequest.class));
    }

    @Test
    void lostPassword_성공() throws Exception {
        // given
        LostPasswordRequest request = LostPasswordRequest.builder()
                .loginId("test")
                .email("test@example.com")
                .build();

        given(adminCommandService.lostPassword(any(LostPasswordRequest.class))).willReturn(true);

        // when & then
        mockMvc.perform(post("/admin/lost/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));

        verify(adminCommandService, times(1)).lostPassword(any(LostPasswordRequest.class));
    }



}