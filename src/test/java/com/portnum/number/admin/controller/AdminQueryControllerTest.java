package com.portnum.number.admin.controller;

import com.portnum.number.admin.service.AdminQueryService;
import com.portnum.number.global.filter.LatencyLoggingFilter;
import com.portnum.number.global.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AdminQueryController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        classes = {LatencyLoggingFilter.class})
        }
)
@AutoConfigureMockMvc(addFilters = false)
class AdminQueryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminQueryService adminQueryService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void healthCheck_성공() throws Exception {
        // when & then
        mockMvc.perform(get("/admin/health"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    void validateEmail_존재() throws Exception {
        // given
        String email = "test@example.com";
        given(adminQueryService.validateEmail(email)).willReturn(true);

        // when & then
        mockMvc.perform(get("/admin/valid/email")
                        .param("value", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void validateEmail_미존재() throws Exception {
        // given
        String email = "invalid-email";
        given(adminQueryService.validateEmail(email)).willReturn(false);

        // when & then
        mockMvc.perform(get("/admin/valid/email")
                        .param("value", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(false));
    }

    @Test
    void validateNickName_성공() throws Exception {
        // given
        String nickName = "validNickName";
        given(adminQueryService.validateNickName(nickName)).willReturn(true);

        // when & then
        mockMvc.perform(get("/admin/valid/nickName")
                        .param("value", nickName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void validateNickName_미존재() throws Exception {
        // given
        String nickName = "invalidNickName";
        given(adminQueryService.validateNickName(nickName)).willReturn(false);

        // when & then
        mockMvc.perform(get("/admin/valid/nickName")
                        .param("value", nickName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(false));
    }

    @Test
    void validateLoginId_존재() throws Exception {
        // given
        String loginId = "validLoginId";
        given(adminQueryService.validateLoginId(loginId)).willReturn(true);

        // when & then
        mockMvc.perform(get("/admin/valid/loginId")
                        .param("value", loginId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    void validateLoginId_미존재() throws Exception {
        // given
        String loginId = "invalidLoginId";
        given(adminQueryService.validateLoginId(loginId)).willReturn(false);

        // when & then
        mockMvc.perform(get("/admin/valid/loginId")
                        .param("value", loginId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(false));
    }

    @Test
    void getNickName_존재() throws Exception {
        // given
        String urlName = "testUrlName";
        String nickName = "testNickName";
        given(adminQueryService.getNickName(urlName)).willReturn(nickName);

        // when & then
        mockMvc.perform(get("/admin/nickName/{urlName}", urlName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(nickName));
    }
}