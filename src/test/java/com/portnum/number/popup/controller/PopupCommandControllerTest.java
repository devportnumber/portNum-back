package com.portnum.number.popup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.portnum.number.admin.controller.AdminCommandController;
import com.portnum.number.global.filter.LatencyLoggingFilter;
import com.portnum.number.global.security.GetLoginIdFromTokenArgumentResolver;
import com.portnum.number.global.security.jwt.JwtAuthenticationFilter;
import com.portnum.number.global.security.jwt.JwtTokenProvider;
import com.portnum.number.global.security.jwt.JwtVerificationFilter;
import com.portnum.number.popup.domain.PopupCategory;
import com.portnum.number.popup.domain.PopupStatus;
import com.portnum.number.popup.domain.embeddable.Address;
import com.portnum.number.popup.domain.embeddable.Point;
import com.portnum.number.popup.dto.request.PopupCreateRequest;
import com.portnum.number.popup.dto.request.PopupModifyRequest;
import com.portnum.number.popup.dto.request.PopupRemoveRequest;
import com.portnum.number.popup.dto.response.PopupDetailResponse;
import com.portnum.number.popup.service.PopupCommandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.BooleanString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = PopupCommandController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        classes = {LatencyLoggingFilter.class})
        }
)
@AutoConfigureMockMvc(addFilters = false)
class PopupCommandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PopupCommandService popupCommandService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private GetLoginIdFromTokenArgumentResolver resolver;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register the JavaTimeModule to handle LocalDateTime
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Ensure dates are serialized as ISO-8601 strings

        given(resolver.supportsParameter(any())).willReturn(true);
        given(resolver.resolveArgument(any(MethodParameter.class), any(ModelAndViewContainer.class),
                any(NativeWebRequest.class), any(WebDataBinderFactory.class)))
                .willReturn("test");

    }

    @Test
    void addPopup_성공() throws Exception{
        //given
        PopupCreateRequest createRequest = PopupCreateRequest.builder()
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

        PopupDetailResponse detailResponse = PopupDetailResponse.builder()
                .popupId(1L)
                .name("새 팝업")
                .build();

        given(popupCommandService.create(any(PopupCreateRequest.class))).willReturn(detailResponse);

        // when & then
        mockMvc.perform(post("/admin/popup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.popupId").value(1L))
                .andExpect(jsonPath("$.data.name").value("새 팝업"));

        verify(popupCommandService, times(1)).create(any(PopupCreateRequest.class));
    }

    @Test
    void modifyPopup_성공() throws Exception{
        PopupModifyRequest modifyRequest = PopupModifyRequest.builder()
                .adminId(1L)
                .popupId(1L)
                .name("수정 팝업")
                .build();

        PopupDetailResponse detailResponse = PopupDetailResponse.builder()
                .popupId(1L)
                .name("수정 팝업")
                .build();

        given(popupCommandService.modify(any(PopupModifyRequest.class), eq("test")))
                .willReturn(detailResponse);

        // when & then
        mockMvc.perform(patch("/admin/popup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(modifyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.popupId").value(1L))
                .andExpect(jsonPath("$.data.name").value("수정 팝업"));

        verify(popupCommandService, times(1))
                .modify(any(PopupModifyRequest.class), eq("test"));

    }

    @Test
    void removePopup_성공() throws Exception{
        //given
        PopupRemoveRequest removeRequest = PopupRemoveRequest.builder()
                .popupIds(List.of(1L))
                .adminId(1L)
                .build();

        willDoNothing().given(popupCommandService).remove(any(PopupRemoveRequest.class), eq("test"));

        //when & then
        mockMvc.perform(delete("/admin/popup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(removeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("Popup remove Success"));

        verify(popupCommandService, times(1)).remove(any(PopupRemoveRequest.class), eq("test"));
    }

}