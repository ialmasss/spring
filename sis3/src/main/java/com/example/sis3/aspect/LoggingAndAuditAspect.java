package com.example.sis3.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAndAuditAspect {

    @Pointcut("execution(* com.example.sis3.service.*.*(..))")
    public void serviceLayerMethods() {}

    @Around("serviceLayerMethods()")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        String methodName = joinPoint.getSignature().toShortString();
        log.info("[AOP-LOG] Entering {} with args: {}", methodName, Arrays.toString(joinPoint.getArgs()));

        Object result = joinPoint.proceed();

        long elapsedMs = System.currentTimeMillis() - start;
        log.info("[AOP-PERF] {} executed in {} ms", methodName, elapsedMs);
        return result;
    }

    @AfterReturning(pointcut = "execution(* com.example.sis3.service.OrderService.createOrder(..))", returning = "result")
    public void auditOrderCreation(JoinPoint joinPoint, Object result) {
        log.info("[AOP-AUDIT] Order creation audited successfully -> {}", result);
    }
}