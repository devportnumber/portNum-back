package com.portnum.number.popup.controller;

import com.portnum.number.global.common.dto.response.DataResponseDto;
import com.portnum.number.global.common.dto.response.PageResponseDto;
import com.portnum.number.popup.dto.PopupSearchCondition;
import com.portnum.number.popup.dto.response.PopupDetailResponse;
import com.portnum.number.popup.service.PopupQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/popup")
public class PopupQueryController {

    private final PopupQueryService popupQueryService;

//    @GetMapping("/api/{adminId}")
//    public DataResponseDto popupList(
//            @PathVariable("adminId") Long adminId,
//            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
//            PopupSearchCondition searchCondition
//    ){
//        PageResponseDto response = popupQueryService.read(adminId, pageNo, searchCondition);
//
//        return DataResponseDto.of(response);
//    }
//
//    @GetMapping("/api/{adminId}/{popupId}")
//    public DataResponseDto popupDetail(@PathVariable("adminId") Long adminId, @PathVariable("popupId") Long popupId){
//        PopupDetailResponse response = popupQueryService.readPopupDetail(adminId, popupId);
//
//        return DataResponseDto.of(response);
//    }

    @GetMapping("/api/{nickName}")
    public DataResponseDto popupList(
            @PathVariable("nickName") String nickName,
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
            PopupSearchCondition searchCondition
    ){
        PageResponseDto response = popupQueryService.read(nickName, pageNo, searchCondition);

        return DataResponseDto.of(response);
    }

    @GetMapping("/api/{nickName}/{popupId}")
    public DataResponseDto popupDetail(@PathVariable("nickName") String nickName, @PathVariable("popupId") Long popupId){
        System.out.println(nickName + " " + popupId + "==================================");
        PopupDetailResponse response = popupQueryService.readPopupDetail(nickName, popupId);

        return DataResponseDto.of(response);
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/supervisor")
    public DataResponseDto portNumAdminRead(
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
            PopupSearchCondition searchCondition
    ){
        PageResponseDto response = popupQueryService.portNumAdminRead(pageNo, searchCondition);

        return DataResponseDto.of(response);
    }
}
