package com.portnum.number.popup.controller;

import com.portnum.number.global.common.dto.response.DataResponseDto;
import com.portnum.number.popup.dto.request.*;
import com.portnum.number.popup.dto.response.ImageResponse;
import com.portnum.number.popup.dto.response.PopupInfoResponse;
import com.portnum.number.popup.service.PopupCommandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/popup")
public class PopupCommandController {

    private final PopupCommandService popupCommandService;

    @PostMapping
    public DataResponseDto addPopup(@Valid @RequestBody PopupCreateRequest request){
        PopupInfoResponse response = popupCommandService.create(request);

        return DataResponseDto.of(response);
    }

    @PatchMapping
    public DataResponseDto modifyPopup(@Valid @RequestBody PopupModifyRequest request){
        PopupInfoResponse response = popupCommandService.modify(request);

        return DataResponseDto.of(response);
    }

    @DeleteMapping
    public DataResponseDto removePopup(@Valid @RequestBody PopupRemoveRequest request){
        popupCommandService.remove(request);

        return DataResponseDto.of("Popup remove Success");
    }

    @PostMapping("/img")
    public DataResponseDto addImages(@Valid @RequestBody ImageAddRequest request){
        PopupInfoResponse response = popupCommandService.addImages(request);

        return DataResponseDto.of(response);
    }

    @PatchMapping("/img")
    public DataResponseDto modifyImages(@Valid @RequestBody ImageModifyRequest request){
        List<ImageResponse> response = popupCommandService.modifyImages(request);

        return DataResponseDto.of(response);
    }

    @DeleteMapping("/img")
    public DataResponseDto removeImages(@Valid @RequestBody ImageRemoveRequest request){
        popupCommandService.removeImages(request);

        return DataResponseDto.of("Image remove Success!");
    }
}
