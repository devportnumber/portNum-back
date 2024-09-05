package com.portnum.number.global.security;

import com.portnum.number.global.exception.Code;
import com.portnum.number.global.exception.GlobalException;
import com.portnum.number.global.security.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class GetEmailFromTokenArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(GetEmailFromToken.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if(request == null){
            throw new GlobalException(Code.NOT_FOUND, "No HttpServletRequest found");
        }

        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader == null){
            throw new GlobalException(Code.UNAUTHORIZED, "Not Found AccessToken");
        }

        String userSubject = jwtTokenProvider.getUserSubject(authorizationHeader.substring(6));

        System.out.println("==================================" + userSubject);
        return userSubject;
    }

}