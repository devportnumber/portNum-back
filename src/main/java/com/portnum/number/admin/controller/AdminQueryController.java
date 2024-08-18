package com.portnum.number.admin.controller;


import com.portnum.number.admin.service.AdminQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminQueryController {

    private final AdminQueryService adminQueryService;
}
