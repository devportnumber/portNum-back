package com.portnum.number.popup.controller;

import com.portnum.number.global.common.dto.response.DataResponseDto;
import com.portnum.number.global.security.GetLoginIdFromToken;
import com.portnum.number.popup.dto.request.*;
import com.portnum.number.popup.dto.response.PopupDetailResponse;
import com.portnum.number.popup.service.PopupCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/popup")
public class PopupCommandController {

    private final PopupCommandService popupCommandService;

    @PostMapping
    public DataResponseDto addPopup(@Valid @RequestBody PopupCreateRequest request){
        PopupDetailResponse response = popupCommandService.create(request);

        return DataResponseDto.of(response);
    }

    @PatchMapping
    public DataResponseDto modifyPopup(@Valid @RequestBody PopupModifyRequest request, @GetLoginIdFromToken String loginId){
        PopupDetailResponse response = popupCommandService.modify(request, loginId);

        return DataResponseDto.of(response);
    }

    @DeleteMapping
    public DataResponseDto removePopup(@Valid @RequestBody PopupRemoveRequest request, @GetLoginIdFromToken String loginId){
        popupCommandService.remove(request, loginId);

        return DataResponseDto.of("Popup remove Success");
    }

}
