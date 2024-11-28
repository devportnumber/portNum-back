package com.portnum.number.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.portnum.number.global.service.RedisService;
import com.portnum.number.global.security.custom.*;
import com.portnum.number.global.security.jwt.*;
import com.portnum.number.global.utils.UrlUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final Aes128Config aes128Config;
    private final RedisService redisService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{

        httpSecurity.httpBasic(HttpBasicConfigurer::disable)
                .headers((headerConfig) -> headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .formLogin(FormLoginConfigurer::disable)
                .logout(LogoutConfigurer::disable)
                .sessionManagement(sessionConfigurer -> sessionConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(entryPointConfigurer -> entryPointConfigurer.authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
                .exceptionHandling(accessDeniedHandler -> accessDeniedHandler.accessDeniedHandler(new CustomAccessDeniedHandler()))
                .with(new CustomFilterConfigurer(), Customizer.withDefaults())
                .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource())) // ⭐️⭐️⭐️
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers(UrlUtils.PERMITTED_URLS.toArray(new String[0])).permitAll()
                                .anyRequest().hasAnyRole( "PORT", "USER", "ADMIN")
                );

        return httpSecurity.build();
    }

    public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedHeaders(Collections.singletonList("*"));
            config.setAllowedMethods(Collections.singletonList("*"));
            config.setAllowedOriginPatterns(Collections.singletonList("*")); // ⭐️ 허용할 origin
            config.setAllowCredentials(true);
            config.setExposedHeaders(List.of("Authorization", "Refresh"));
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", config);

            return source;
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);
            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager,
                    jwtTokenProvider, redisService, aes128Config);

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenProvider, redisService);

            jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new LoginSuccessHandler(objectMapper));
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new LoginFailureHandler());

            builder
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                    .addFilterBefore(new JwtReissueTokenFilter(jwtTokenProvider, redisService, aes128Config), JwtAuthenticationFilter.class)
                    .addFilterBefore(new JwtExceptionFilter(objectMapper), JwtReissueTokenFilter.class)
                    .addFilterAfter(new CustomLogoutFilter(redisService, jwtTokenProvider), JwtAuthenticationFilter.class)
                    .addFilterAfter(jwtVerificationFilter, CustomLogoutFilter.class);
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}

