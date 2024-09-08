package com.portnum.number.global.aop;

import com.portnum.number.global.aop.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.Method;

@Slf4j
@Aspect
public class RetryAspect {

    @Around("@annotation(retry)")
    public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable{
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        log.info("[retry] Method: {}, retry={}", method.getName(), retry);

        int maxRetry = retry.value();
        Exception exceptionHandler = null;

        for(int retryCount = 1; retryCount <= maxRetry; retryCount++){
            try{
                log.info("[retry] try count = {}/{}", retryCount, maxRetry);
                return joinPoint.proceed();
            } catch (Exception e){
                exceptionHandler = e;
            }
        }

        throw exceptionHandler;
    }
}
