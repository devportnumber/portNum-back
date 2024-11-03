package com.portnum.number.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portnum.number.global.common.dto.response.ResponseDto;
import com.portnum.number.global.exception.AccountException;
import com.portnum.number.global.exception.FilterException;
import com.portnum.number.global.exception.GlobalException;
import com.portnum.number.global.exception.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException | AccountException e) {
//            System.out.println("==========================");
            setExceptionResponse(response, e);
        }
    }

    private void setExceptionResponse(HttpServletResponse response, FilterException e) throws IOException {
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter()
                .write(
                        objectMapper.writeValueAsString(
                                new ResponseDto(false, e.getErrorCode().getCode(), e.getMessage())
                        )
                );
    }
}
