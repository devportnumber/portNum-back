package com.portnum.number.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class AopTrace {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) && !within(@com.portnum.number.global.aop.NoTrace *)")
    public void controller(){
    }

    @Before("controller()")
    public void doTrace(JoinPoint joinPoint){
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        log.info("className : {}, methodName : {}", methodSignature.getClass().getName(), methodSignature.getMethod().getName());

        // 추후 Authorization 로그 추가할 경우 추가
    }
}
