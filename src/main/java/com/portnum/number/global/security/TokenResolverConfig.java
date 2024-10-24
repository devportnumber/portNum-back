package com.portnum.number.global.security;

import com.querydsl.core.annotations.Config;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class TokenResolverConfig implements WebMvcConfigurer {

    private final GetEmailFromTokenArgumentResolver getEmailFromTokenArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(getEmailFromTokenArgumentResolver);
    }
}
