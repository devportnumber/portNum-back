package com.portnum.number.global.security.custom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portnum.number.global.common.dto.response.ResponseDto;
import com.portnum.number.global.exception.Code;
import com.portnum.number.global.exception.JwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error("인증되지 않은 사용자 접근");
        throw new JwtException(Code.UNAUTHORIZED, "인증되지 않은 사용자의 접근");
    }

}