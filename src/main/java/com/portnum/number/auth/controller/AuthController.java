package com.portnum.number.auth.controller;

import com.portnum.number.auth.service.AuthService;
import com.portnum.number.global.common.dto.response.DataResponseDto;
import com.portnum.number.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PatchMapping("/logout")
    public DataResponseDto logout(HttpServletRequest request){
        authService.logout(request);

        return DataResponseDto.of("Logout Success!");
    }

    @PatchMapping("/reissue")
    public DataResponseDto reissue(HttpServletRequest request, HttpServletResponse response){
        authService.reissueAccessToken(request, response);

        return DataResponseDto.of("Reissue Success!");
    }
}