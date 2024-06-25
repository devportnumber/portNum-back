package com.portnum.number.admin.controller;

import com.portnum.number.admin.service.AdminListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * admin API
 *
 * @author 안지은
 */
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class MainController {

    private final AdminListService adminListService;

    @GetMapping("/list")
    public ResponseEntity<?> doGetAsList() {
        return ResponseEntity.ok(adminListService.getAdminList());
    }
}
