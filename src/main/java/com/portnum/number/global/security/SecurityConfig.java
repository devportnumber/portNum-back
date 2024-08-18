//package com.portnum.number.global.security;
//
//import com.portnum.number.global.security.jwt.JwtSecurityConfig;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
//import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
//import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.CorsConfigurationSource;
//
//import java.util.Collections;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(securedEnabled = true)
//@RequiredArgsConstructor
//public class SecurityConfig {
//
//    private final JwtTokenProvider jwtTokenProvider;
//    private final UserService userService;
//    private final AES128Config aes128Config;
//    private final RedisService redisService;
//
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
//        httpSecurity.httpBasic(HttpBasicConfigurer::disable)
//                .headers((headerConfig) -> headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
//                .formLogin(FormLoginConfigurer::disable)
//                .sessionManagement(sessionConfigurer -> sessionConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .exceptionHandling(entryPointConfigurer -> entryPointConfigurer.authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
//                .exceptionHandling(accessDeniedHandler -> accessDeniedHandler.accessDeniedHandler(new CustomAccessDeniedHandler()))
//                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource())) // ⭐️⭐️⭐️
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authorize ->
//                        authorize
//                                .requestMatchers("/login").permitAll()
//                                .anyRequest().authenticated()
//                )
//                .addFilterBefore(new JwtSecurityConfig(jwtTokenProvider, userService, aes128Config, redisService));
//
//        return httpSecurity.build();
//    }
//
//    CorsConfigurationSource corsConfigurationSource() {
//        return request -> {
//            CorsConfiguration config = new CorsConfiguration();
//            config.setAllowedHeaders(Collections.singletonList("*"));
//            config.setAllowedMethods(Collections.singletonList("*"));
//            config.setAllowedOriginPatterns(Collections.singletonList("http://localhost:3000")); // ⭐️ 허용할 origin
//            config.setAllowCredentials(true);
//            return config;
//        };
//    }
//
//}
