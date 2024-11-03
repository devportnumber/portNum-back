package com.portnum.number.global.security.jwt;

import com.portnum.number.global.common.service.RedisService;
import com.portnum.number.global.exception.Code;
import com.portnum.number.global.exception.JwtException;
import com.portnum.number.global.utils.UrlUtils;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException, java.io.IOException {
        String accessToken = jwtTokenProvider.resolveAccessToken(request);

        if(!StringUtils.hasText(accessToken)){
            throw new JwtException(Code.TOKEN_ERROR, "Empty Access Token");
        }

        try {
            if(jwtTokenProvider.validateToken(accessToken) && doNotLogout(accessToken)){
                    setAuthenticationToContext(accessToken, response);
            } else{
                filterChain.doFilter(request, response);
                return;
            }
        } catch (JwtException e) {
//            ObjectMapper objectMapper = new ObjectMapper();
//            response.setCharacterEncoding("utf-8");
//            response.setStatus(HttpStatus.OK.value());
//            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//            response.getWriter().write(objectMapper.writeValueAsString(new ResponseDto(false, Code.VALIDATION_ERROR.getCode(), "Not Valid AccessToken")));
            throw new JwtException(e.getErrorCode(), e.getMessage());
        }
        filterChain.doFilter(request, response);
    }


    private void setAuthenticationToContext(String accessToken, HttpServletResponse response) throws java.io.IOException {
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken, response);
        log.info("Authentication: {}", authentication);
//        System.out.println(authentication.getPrincipal().toString());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("# Token verification success!");
    }

    private boolean doNotLogout(String accessToken) {
        String isLogout = redisService.getValues(accessToken);
//        log.info("isLogout: " + isLogout);
        return isLogout.equals("false");
    }

    // EXCLUDE_URL과 동일한 요청이 들어왔을 경우, 현재 필터를 진행하지 않고 다음 필터 진행
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String servletPath = request.getServletPath();
        return UrlUtils.EXCLUDE_URLS.stream().anyMatch(servletPath::startsWith);
    }
}