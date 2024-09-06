package com.portnum.number.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
public class AopTrace {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controller(){
    }

    @Before("controller()")
    public void doTrace(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        // @NoTrace 어노테이션이 있는지 확인
        if (method.isAnnotationPresent(NoTrace.class)) {
            return; // NoTrace가 붙은 메서드는 로그 출력하지 않음
        }

        log.info("className : {}, methodName : {}", methodSignature.getClass().getName(), methodSignature.getMethod().getName());

        // 추후 Authorization 로그 추가할 경우 추가
    }
}
