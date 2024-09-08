package com.portnum.number.admin.controller;


import com.portnum.number.admin.service.AdminQueryService;
import com.portnum.number.global.aop.annotation.NoTrace;
import com.portnum.number.global.common.dto.response.DataResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminQueryController {

    private final AdminQueryService adminQueryService;

    @NoTrace
    @GetMapping("/health")
    public String healthCheck(){
        return "ok";
    }

    @GetMapping("/valid/email")
    public DataResponseDto validateEmail(@RequestParam("value") String email){
        boolean response = adminQueryService.validateEmail(email);

        return DataResponseDto.of(response);
    }

    @GetMapping("/valid/nickName")
    public DataResponseDto validateNickName(@RequestParam("value") String nickName){
        boolean response = adminQueryService.validateNickName(nickName);

        return DataResponseDto.of(response);
    }

}
