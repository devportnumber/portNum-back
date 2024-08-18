package com.portnum.number.admin.controller;

import com.portnum.number.admin.dto.request.AdminCreateRequest;
import com.portnum.number.admin.dto.request.AdminModifyRequest;
import com.portnum.number.admin.dto.response.AdminInfoResponse;
import com.portnum.number.admin.service.AdminCommandService;
import com.portnum.number.global.common.dto.response.DataResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminCommandController {

    private final AdminCommandService adminCommandService;

    @PostMapping
    public DataResponseDto addAdmin(AdminCreateRequest request){
        AdminInfoResponse response = adminCommandService.create(request);

        return DataResponseDto.of(response);
    }

    @PatchMapping
    public DataResponseDto modifyAdmin(AdminModifyRequest request){
        AdminInfoResponse response = adminCommandService.modify(request);

        return DataResponseDto.of(response);
    }

}
