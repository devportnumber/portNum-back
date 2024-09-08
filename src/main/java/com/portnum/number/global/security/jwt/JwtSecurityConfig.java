//package com.portnum.number.global.security.jwt;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.DefaultSecurityFilterChain;
//
//@RequiredArgsConstructor
//public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
//    private final JwtTokenProvider jwtTokenProvider;
//    private final UserService userService;
//    private final AES128Config aes128Config;
//    private final RedisService redisService;
//
//    public JwtSecurityConfig(JwtTokenProvider jwtTokenProvider, UserService userService, AES128Config aes128Config, RedisService redisService) {
//
//    }
//
//    @Override
//    public void configure(HttpSecurity http){
//        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
//        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager,
//                jwtTokenProvider, userService, redisService, aes128Config);
//
//        JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenProvider, redisService);
//
//        jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");
//        jwtAuthenticationFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler());
//        jwtAuthenticationFilter.setAuthenticationFailureHandler(new LoginFailurHandler());
//
//        http
//                .addFilter(jwtAuthenticationFilter)
//                .addFilterAfter(jwtVerificationFilter, JwtAuthenticationFilter.class);
//    }
//}
