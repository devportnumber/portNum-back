package com.portnum.number.auth.service;

import com.portnum.number.admin.entity.Admin;
import com.portnum.number.admin.repository.AdminRepository;
import com.portnum.number.global.common.config.Aes128Config;
import com.portnum.number.global.common.dto.TokenDto;
import com.portnum.number.global.common.service.RedisService;
import com.portnum.number.global.exception.Code;
import com.portnum.number.global.exception.GlobalException;
import com.portnum.number.global.security.custom.CustomUserDetails;
import com.portnum.number.global.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final Aes128Config aes128Config;
    private final RedisService redisService;
    private final AdminRepository adminRepository;

    @Transactional
    public void logout(HttpServletRequest request) {
        String loginId = extractLoginId(request);

        String redisRefreshToken = redisService.getValues(loginId);
        if(redisService.checkExistsValue(redisRefreshToken)){
            redisService.deleteValues(loginId);

            // 로그아웃 시 Access Token Redis 저장 ( key = Access Token / value = "logout")
            long accessTokenValidityInSeconds = jwtTokenProvider.getAccessTokenValidityInSeconds();
            redisService.setValues(jwtTokenProvider.resolveAccessToken(request), "logout", Duration.ofMillis(accessTokenValidityInSeconds));
        }
    }

    public void reissueAccessToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = aes128Config.decryptAes(jwtTokenProvider.resolveRefreshToken(request));

        try{
            if(jwtTokenProvider.validateToken(refreshToken, response)){
                String loginId = extractLoginId(request);
                String redisRefreshToken = redisService.getValues(loginId);

                if(redisService.checkExistsValue(redisRefreshToken) && refreshToken.equals(redisRefreshToken)){
                    Admin findUser = findByLoginId(loginId);
                    CustomUserDetails userDetails = CustomUserDetails.of(findUser);
                    TokenDto tokenDto = jwtTokenProvider.generateToken(userDetails);
                    jwtTokenProvider.accessTokenSetHeader(tokenDto.getAccessToken(), response);
                    return;
                }

                throw new GlobalException(Code.REISSUE_FAIL, "토큰 재발급에 실패했습니다.");
            }
        } catch (Exception e){
            throw new GlobalException(Code.NOT_VALID_REFRESH_TOKEN, "리프레시 토큰이 유효하지 않습니다.");
        }
    }

    private Admin findByEmail(String email){
        return adminRepository.findByEmail(email)
                .orElseThrow(() -> new GlobalException(Code.NOT_FOUND, "Not Found Admin Email"));
    }

    private Admin findByLoginId(String loginId){
        return adminRepository.findByLoginId(loginId)
                .orElseThrow(() -> new GlobalException(Code.NOT_FOUND, "Not Found Admin Email"));
    }


    public String extractEmail(HttpServletRequest request) {
        String encryptedRefreshToken = aes128Config.decryptAes(jwtTokenProvider.resolveRefreshToken(request));
        Claims claims = jwtTokenProvider.parseClaims(encryptedRefreshToken);

        return claims.getSubject();
    }

    public String extractLoginId(HttpServletRequest request) {
        String encryptedRefreshToken = aes128Config.decryptAes(jwtTokenProvider.resolveRefreshToken(request));
        Claims claims = jwtTokenProvider.parseClaims(encryptedRefreshToken);

        return claims.getSubject();
    }

}