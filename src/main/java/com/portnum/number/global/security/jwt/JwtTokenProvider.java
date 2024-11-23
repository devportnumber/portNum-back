package com.portnum.number.global.security.jwt;

import com.portnum.number.admin.domain.RoleType;
import com.portnum.number.global.common.dto.TokenDto;
import com.portnum.number.global.common.enums.ExpiredTimeEnum;
import com.portnum.number.global.exception.Code;
import com.portnum.number.global.exception.JwtException;
import com.portnum.number.global.security.custom.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import io.jsonwebtoken.Jwts;

@Slf4j
@Component
public class JwtTokenProvider {

    public static final String BEARER = "Bearer";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String REFRESH_HEADER = "Refresh";
    public static final String BEARER_PREFIX = "Bearer ";
    private static final String AUTHORITIES_KEY = "auth";

    private final SecretKey secretKey;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secret){
        this.secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    @Getter
    @Value("${jwt.accessToken-validity-in-seconds}")
    private long accessTokenValidityInSeconds;

    @Getter
    @Value("${jwt.refreshToken-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;



    // 유저 정보를 토대로 AccessToken, RefreshToken 생성
    public TokenDto generateToken(CustomUserDetails customUserDetails){
        // AccessToken 생성
        String accessToken = this.createToken(
                customUserDetails.getUsername(), customUserDetails.getRoleType().getRoleType(), ExpiredTimeEnum.ACCESS_TOKEN
        );

        // RefreshToken 생성
        String refreshToken = this.createToken(
                customUserDetails.getUsername(), customUserDetails.getRoleType().getRoleType(), ExpiredTimeEnum.REFRESH_TOKEN
        );

        return TokenDto.builder()
                .grantType(BEARER)
                .authorizationType(AUTHORIZATION_HEADER)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpiresIn(ExpiredTimeEnum.ACCESS_TOKEN.getExpiredTime())
                .build();
    }

    private String createToken(String userName, String role, ExpiredTimeEnum expiredTimeEnum){
        return Jwts.builder()
                .claim(AUTHORITIES_KEY, role)
                .subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredTimeEnum.getExpiredTime()))
                .signWith(secretKey)
                .compact();
    }

    public Authentication getAuthentication(String accessToken, HttpServletResponse response) throws IOException {
        // 토큰 복호화
        String roleString = getRole(accessToken);

        if(!StringUtils.hasText(roleString)){
            throw new JwtException(Code.UNAUTHORIZED, "Empty Authorized Token");
        }

        RoleType role;
        if(roleString.equals("ROLE_USER"))
            role = RoleType.INFLUENCER;
        else if(roleString.equals("ROLE_PORT"))
            role = RoleType.PORT;
        else
            role = RoleType.ADMIN;


        CustomUserDetails customUserDetails =
                CustomUserDetails.of(getUserSubject(accessToken), role);


        log.info("# AuthUser.getRoles 권한 체크 = {}", customUserDetails.getAuthorities().toString());

        return new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

    }

    // 토큰 정보 검증
    public boolean validateToken(String token) throws IOException {
        try{
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return true;
        } catch(MalformedJwtException e){
            log.trace("Invalid JWT token trace = {}", e);
            throw new com.portnum.number.global.exception.JwtException(Code.TOKEN_ERROR, "손상된 토큰입니다.");
        } catch (ExpiredJwtException e){
            log.trace("Expired JWT token trace = {}", e);
            throw new com.portnum.number.global.exception.JwtException(Code.EXPIRE_ACCESS_TOKEN, "만료된 토큰입니다.");
        } catch (UnsupportedJwtException e){
            log.trace("Unsupported JWT token trace = {}", e);
            throw new com.portnum.number.global.exception.JwtException(Code.TOKEN_ERROR, "지원하지 않는 토큰입니다.");
        } catch(IllegalArgumentException e){
            log.trace("JWT claims string is empty trace = {}", e);
            throw new JwtException(Code.TOKEN_ERROR, "시그니처 검증에 실패한 토큰입니다.");
        } catch (SignatureException e){
            log.trace("Invalid JWT token trace = {}", e);
            throw new com.portnum.number.global.exception.JwtException(Code.TOKEN_ERROR, "손상된 토큰입니다.");
        }
    }

    public void accessTokenSetHeader(String accessToken, HttpServletResponse response){
        String headerValue = BEARER_PREFIX + accessToken;
        response.setHeader(AUTHORIZATION_HEADER, headerValue);
    }

    public void refreshTokenSetHeader(String refreshToken, HttpServletResponse response){
        response.setHeader(REFRESH_HEADER, refreshToken);
    }

    // Request Header의 Access Token 정보 추출
    public String resolveAccessToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.split(BEARER_PREFIX)[1];
        }

        return null;
    }

    // Request Header에 Refresh Token 정보 추출
    public String resolveRefreshToken(HttpServletRequest request){
        String refreshToken = request.getHeader(REFRESH_HEADER);
        if(StringUtils.hasText(refreshToken)){
            return refreshToken;
        }


        throw new JwtException(Code.EMPTY_REFRESH_TOKEN, "리프레시 토큰이 존재하지 않습니다.");
    }

    public String getUserSubject(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public String getRole(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get(AUTHORITIES_KEY, String.class);
    }

    public Boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date());
    }

}
