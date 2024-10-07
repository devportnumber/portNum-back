package com.portnum.number.admin.controller;

import com.portnum.number.admin.dto.request.*;
import com.portnum.number.admin.dto.response.AdminInfoResponse;
import com.portnum.number.admin.service.AdminCommandService;
import com.portnum.number.global.common.dto.response.DataResponseDto;
import com.portnum.number.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminCommandController {

    private final AdminCommandService adminCommandService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public DataResponseDto addAdmin(@Valid @RequestBody AdminCreateRequest request){
        AdminInfoResponse response = adminCommandService.create(request);

        return DataResponseDto.of(response);
    }

    @PatchMapping
    public DataResponseDto modifyAdmin(@Valid @RequestBody AdminModifyRequest request){
        AdminInfoResponse response = adminCommandService.modify(request);

        return DataResponseDto.of(response);
    }

    @PatchMapping("/password")
    public DataResponseDto modifyPassword(@Valid @RequestBody AdminModifyPasswordRequest request, HttpServletRequest httpServletRequest){
        boolean response = adminCommandService.modifyPassword(request, jwtTokenProvider.resolveAccessToken(httpServletRequest));

        return DataResponseDto.of(response);
    }

    @PostMapping("/lost/email")
    public DataResponseDto lostEmail(@Valid @RequestBody LostRequest request){
        boolean response = adminCommandService.lostEmail(request);

        return DataResponseDto.of(response);
    }

    @PostMapping("/lost/loginId")
    public DataResponseDto lostLoginId(@Valid @RequestBody LostLoginIdRequest request){
        String response = adminCommandService.lostLoginId(request);

        return DataResponseDto.of(response);
    }

    @PostMapping("/lost/password")
    public DataResponseDto lostPassword(@Valid @RequestBody LostPasswordRequest request){
        boolean response = adminCommandService.lostPassword(request);

        return DataResponseDto.of(response);
    }

}
