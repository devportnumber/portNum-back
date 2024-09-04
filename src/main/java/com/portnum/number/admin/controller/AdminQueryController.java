package com.portnum.number.admin.controller;


import com.portnum.number.admin.service.AdminQueryService;
import com.portnum.number.global.aop.NoTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
