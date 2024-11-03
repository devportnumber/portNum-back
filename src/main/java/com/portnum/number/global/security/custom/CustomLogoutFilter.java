package com.portnum.number.global.security.custom;

import com.portnum.number.global.common.dto.response.ResponseDto;
import com.portnum.number.global.common.enums.ExpiredTimeEnum;
import com.portnum.number.global.common.service.RedisService;
import com.portnum.number.global.exception.Code;
import com.portnum.number.global.exception.JwtException;
import com.portnum.number.global.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
public class CustomLogoutFilter extends OncePerRequestFilter {
    private final RedisService redisService;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                          FilterChain filterChain) throws IOException, ServletException {
        String requestURI = request.getRequestURI();
        String requestMethod = request.getMethod();

        if (!requestURI.matches("/auth/logout") || !requestMethod.equals("PATCH")) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = jwtTokenProvider.resolveAccessToken(request);

        String loginId;
        try{
            loginId = jwtTokenProvider.getUserSubject(accessToken);
        } catch (ExpiredJwtException e){
            throw new JwtException(Code.EXPIRE_ACCESS_TOKEN, "Access Token is Expired");
        }

        // 로그인 ID에 해당하는 Redis 값 삭제
        redisService.deleteValues(loginId);

        // 로그아웃 시 Access Token Redis 저장 (key = Access Token / value = "logout")
        long accessTokenValidityInSeconds = ExpiredTimeEnum.ACCESS_TOKEN.getExpiredTime();
        redisService.setValues(accessToken, "logout", Duration.ofMillis(accessTokenValidityInSeconds));

        // 인증 컨텍스트 초기화
        SecurityContextHolder.clearContext();

        // 헤더 초기화
        response.setHeader(JwtTokenProvider.AUTHORIZATION_HEADER, null);
        response.setHeader(JwtTokenProvider.REFRESH_HEADER, null);

        response.setStatus(HttpStatus.OK.value());
    }
}

