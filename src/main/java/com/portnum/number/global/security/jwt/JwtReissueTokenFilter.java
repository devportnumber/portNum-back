package com.portnum.number.global.security.jwt;

import com.portnum.number.admin.domain.RoleType;
import com.portnum.number.global.common.dto.TokenDto;
import com.portnum.number.global.service.RedisService;
import com.portnum.number.global.config.Aes128Config;
import com.portnum.number.global.exception.Code;
import com.portnum.number.global.exception.JwtException;
import com.portnum.number.global.security.custom.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtReissueTokenFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;
    private final Aes128Config aes128Config;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath();
        String method = request.getMethod();

        if(path.equals("/auth/reissue") && method.equals("PATCH")){
            String accessToken = jwtTokenProvider.resolveAccessToken(request);

            boolean isExpired;

            try{
                isExpired = jwtTokenProvider.isExpired(accessToken);
            } catch (ExpiredJwtException e){
                isExpired = true;
            }

            if(!isExpired){
                jwtTokenProvider.accessTokenSetHeader(accessToken, response);
                return;
            }

            String refreshToken = aes128Config.decryptAes(jwtTokenProvider.resolveRefreshToken(request));

//            System.out.println(refreshToken);
            if(!StringUtils.hasText(refreshToken))
                throw new JwtException(Code.EMPTY_REFRESH_TOKEN, "Refresh Token is Empty");

            try{
                jwtTokenProvider.isExpired(refreshToken);
            } catch (ExpiredJwtException e){
                throw new JwtException(Code.EXPIRE_REFRESH_TOKEN, "Refresh Token is Expired");
            }

            String loginId = jwtTokenProvider.getUserSubject(refreshToken);
            String redisRefreshToken = redisService.getValues(loginId);

//            System.out.println(loginId + " " + redisRefreshToken);

            if(redisService.checkExistsValue(redisRefreshToken) && refreshToken.equals(redisRefreshToken)){
                String role = jwtTokenProvider.getRole(refreshToken);
                CustomUserDetails userDetails = CustomUserDetails.of(loginId, RoleType.fromRoleType(role));
                TokenDto tokenDto = jwtTokenProvider.generateToken(userDetails);
                jwtTokenProvider.accessTokenSetHeader(tokenDto.getAccessToken(), response);
                return;
            }

            throw new JwtException(Code.REISSUE_FAIL, "토큰 재발급에 실패했습니다.");
        }

        filterChain.doFilter(request, response);
    }
}
