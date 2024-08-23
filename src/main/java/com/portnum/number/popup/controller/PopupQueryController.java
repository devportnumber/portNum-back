package com.portnum.number.popup.controller;

import com.portnum.number.global.common.dto.response.DataResponseDto;
import com.portnum.number.global.common.dto.response.PageResponseDto;
import com.portnum.number.popup.dto.PopupSearchCondition;
import com.portnum.number.popup.dto.response.PopupDetailResponse;
import com.portnum.number.popup.service.PopupQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/popup")
public class PopupQueryController {

    private final PopupQueryService popupQueryService;

    @GetMapping("/{adminId}")
    public DataResponseDto popupList(
            @PathVariable("adminId") Long adminId,
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
            PopupSearchCondition searchCondition
    ){
        PageResponseDto response = popupQueryService.read(adminId, pageNo, searchCondition);

        return DataResponseDto.of(response);
    }

    @GetMapping("/{adminId}/{popupId}")
    public DataResponseDto popupInfo(@PathVariable("adminId") Long adminId, @PathVariable("popupId") Long popupId){
        PopupDetailResponse response = popupQueryService.readPopupInfo(adminId, popupId);

        return DataResponseDto.of(response);
    }
}
