package com.portnum.number.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portnum.number.admin.entity.Admin;
import com.portnum.number.admin.service.AdminQueryService;
import com.portnum.number.global.common.config.Aes128Config;
import com.portnum.number.global.common.dto.LoginDto;
import com.portnum.number.global.common.dto.TokenDto;
import com.portnum.number.global.common.service.RedisService;
import com.portnum.number.global.security.custom.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AdminQueryService adminQueryService;
    private final RedisService redisService;
    private final Aes128Config aes128Config;

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // ServletInputStream을 LoginDto에 담기 위해 역직렬화 수행
        ObjectMapper objectMapper = new ObjectMapper();
        LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getLoginId(), loginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal(); // CustomUserDetails 클래스의 객체를 얻음
        TokenDto tokenDto = jwtTokenProvider.generateToken(customUserDetails);

        String accessToken = tokenDto.getAccessToken();
        String refreshToken = tokenDto.getRefreshToken();

        String encryptedRefreshToken = aes128Config.encryptAes(refreshToken);

        jwtTokenProvider.accessTokenSetHeader(accessToken, response);
        jwtTokenProvider.refreshTokenSetHeader(encryptedRefreshToken, response);

//        Admin findAdmin = adminQueryService.findAdmin(customUserDetails.getId());
//        Responder.loginSuccessResponse(response, findUser)

        // 로그인 성공시 Refresh Token Redis 저장(key = Email / value = Refresh Token)
        long refreshTokenValidityInSeconds = jwtTokenProvider.getRefreshTokenValidityInSeconds();
        redisService.setValues(customUserDetails.getLoginId(), refreshToken, Duration.ofMillis(refreshTokenValidityInSeconds));

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }
}
