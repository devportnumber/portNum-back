package com.portnum.number.auth.service;

import com.portnum.number.admin.repository.AdminRepository;
import com.portnum.number.global.common.config.Aes128Config;
import com.portnum.number.global.common.service.RedisService;
import com.portnum.number.global.exception.Code;
import com.portnum.number.global.exception.GlobalException;
import com.portnum.number.global.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    public void logout(String encryptedRefreshToken, String accessToken) {
        verifiedRefreshToken(encryptedRefreshToken);

        String refreshToken = aes128Config.decryptAes(encryptedRefreshToken);
        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
        String email = claims.getSubject();
        String redisRefreshToken = redisService.getValues(email);

        if(redisService.checkExistsValue(redisRefreshToken)){
            redisService.deleteValues(email);
            // 로그아웃 시 Access Token Redis 저장 ( key = Access Token / value = "logout")
            long accessTokenValidityInSeconds = jwtTokenProvider.getAccessTokenValidityInSeconds();
            redisService.setValues(accessToken, "logout", Duration.ofMillis(accessTokenValidityInSeconds));
        }
    }


    private void verifiedRefreshToken(String encryptedRefreshToken){
        if(!StringUtils.hasText(encryptedRefreshToken)){
            throw new GlobalException(Code.VALIDATION_ERROR, "Not Valid RefreshToken");
        }
    }

//    private User findUserByEmail(String email){
//        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new BusinessLogicException(ErrorCode.USER_NOT_FOUND));
//    }
//
//    public String reissueAccessToken(String encryptedRefreshToken) {
//        this.verifiedRefreshToken(encryptedRefreshToken);
//        String refreshToken = aes128Config.decryptAes(encryptedRefreshToken);
//        Claims claims = jwtTokenProvider.parseClaims(refreshToken);
//        String email = claims.getSubject();
//        String redisRefreshToken = redisService.getValues(email);
//
//        if(redisService.checkExistsValue(redisRefreshToken) && refreshToken.equals(redisRefreshToken)){
//            User findUser = this.findUserByEmail(email);
//            CustomUserDetails userDetails = CustomUserDetails.of(findUser);
//            TokenDto tokenDto = jwtTokenProvider.generateToken(userDetails);
//            String newAccessToken = tokenDto.getAccessToken();
////            long refreshTokenExpirationMillis = jwtTokenProvider.getRefreshTokenValidityInSeconds();
////            redisService.setValues(email, redisRefreshToken, Duration.ofMillis(refreshTokenExpirationMillis));
//
//            return newAccessToken;
//        }
//        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
//
//    }


}