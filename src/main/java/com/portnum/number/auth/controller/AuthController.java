package com.portnum.number.auth.controller;

import com.portnum.number.auth.service.AuthService;
import com.portnum.number.global.common.dto.response.DataResponseDto;
import com.portnum.number.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PatchMapping("/logout")
    public DataResponseDto logout(HttpServletRequest request){
        authService.logout(jwtTokenProvider.resolveRefreshToken(request), jwtTokenProvider.resolveAccessToken(request));

        return DataResponseDto.of("Logout Success!");
    }

//    @PatchMapping("/reissue")
//    public ResponseEntity reissue(HttpServletRequest request, HttpServletResponse response){
//        String encryptedRefreshToken = jwtTokenProvider.resolveRefreshToken(request);
//        String newAccessToken = authService.reissueAccessToken(encryptedRefreshToken);
//        jwtTokenProvider.accessTokenSetHeader(newAccessToken, response);
//
//        return new ResponseEntity<>(new SingleResponseDto<>("Access Token reissued"), HttpStatus.OK);
//    }
}