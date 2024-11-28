package com.portnum.number.global.security.jwt;

import com.portnum.number.admin.domain.RoleType;
import com.portnum.number.global.common.dto.TokenDto;
import com.portnum.number.global.exception.JwtException;
import com.portnum.number.global.security.custom.CustomUserDetails;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;
    private CustomUserDetails customUserDetails;
    private String secretKey;

    @BeforeEach
    void setUp() {
        secretKey = "port-number-secret-key-for-test-in-jwt-provider-test";
        jwtTokenProvider = new JwtTokenProvider(secretKey);

        customUserDetails = CustomUserDetails.of("testUser", RoleType.PORT);
    }

    @Test
    void generateToken_테스트() throws Exception{
        //when
        TokenDto tokenDto = jwtTokenProvider.generateToken(customUserDetails);

        //then
        assertThat(tokenDto.getAccessToken()).isNotBlank();
        assertThat(tokenDto.getRefreshToken()).isNotBlank();
        assertThat(tokenDto.getGrantType()).isEqualTo("Bearer");
    }

    @Test
    void getAuthentication_테스트() throws Exception {
        //given
        TokenDto tokenDto = jwtTokenProvider.generateToken(customUserDetails);

        //when
        Authentication authentication = jwtTokenProvider.getAuthentication(tokenDto.getAccessToken(), new MockHttpServletResponse());

        //then
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo("testUser");
    }

    @Test
    void validateToken_성공_테스트() throws Exception {
        //given
        String accessToken = jwtTokenProvider.generateToken(customUserDetails).getAccessToken();

        //when
        boolean isValid = jwtTokenProvider.validateToken(accessToken);

        //then
        assertThat(isValid).isTrue();
    }

    @Test
    void validateToken_Malformed_실패_테스트() throws Exception {
        //given
        String accessToken = "malformedAccessToken";

        //when & then
        assertThrows(JwtException.class, () -> jwtTokenProvider.validateToken(accessToken));
    }

    @Test
    void validateToken_Expired_실패_테스트() throws Exception{
        // given
        String expiredToken = Jwts.builder()
                .subject("testUser")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm()))
                .compact();

        //when & then
        assertThrows(JwtException.class, () -> jwtTokenProvider.validateToken(expiredToken));
    }

    @Test
    void validateToken_EmptyClaims_실패_테스트() throws Exception {
        // given
        String emptyClaimsToken = "";

        // when & then
        assertThrows(JwtException.class, () -> jwtTokenProvider.validateToken(emptyClaimsToken));
    }

    @Test
    void validateToken_SignatureInvalid_실패_테스트() throws Exception {
        // given
        String tamperedToken = Jwts.builder()
                .subject("testUser")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 10000))
                .signWith(new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm()))
                .compact();

        tamperedToken += "tampered";

        // when & then
        String finalTamperedToken = tamperedToken;
        assertThrows(JwtException.class, () -> jwtTokenProvider.validateToken(finalTamperedToken));
    }

    @Test
    void resolveAccessToken_테스트() throws Exception{
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String accessToken = jwtTokenProvider.generateToken(customUserDetails).getAccessToken();
        request.addHeader(JwtTokenProvider.AUTHORIZATION_HEADER, JwtTokenProvider.BEARER_PREFIX + accessToken);

        // when
        String resolvedToken = jwtTokenProvider.resolveAccessToken(request);

        // then
        assertThat(resolvedToken).isEqualTo(accessToken);
    }

    @Test
    void resolveRefreshToken_테스트() throws Exception{
        // given
        MockHttpServletRequest request = new MockHttpServletRequest();
        String refreshToken = jwtTokenProvider.generateToken(customUserDetails).getRefreshToken();
        request.addHeader(JwtTokenProvider.REFRESH_HEADER, refreshToken);

        // when
        String resolvedToken = jwtTokenProvider.resolveRefreshToken(request);

        // then
        assertThat(resolvedToken).isEqualTo(refreshToken);
    }

    @Test
    void getUserSubject_테스트() throws Exception{
        // given
        String accessToken = jwtTokenProvider.generateToken(customUserDetails).getAccessToken();

        // when
        String userSubject = jwtTokenProvider.getUserSubject(accessToken);

        // then
        assertThat(userSubject).isEqualTo("testUser");
    }

    @Test
    void getRole_테스트() throws Exception{
        // given
        String accessToken = jwtTokenProvider.generateToken(customUserDetails).getAccessToken();

        // when
        String role = jwtTokenProvider.getRole(accessToken);

        // then
        assertThat(role).isEqualTo("ROLE_PORT");
    }


    @Test
    void isExpired_False_테스트() throws Exception{
        //given
        String accessToken = jwtTokenProvider.generateToken(customUserDetails).getAccessToken();
        //when
        boolean isExpired = jwtTokenProvider.isExpired(accessToken);
        //then
        assertThat(isExpired).isFalse();
    }


}