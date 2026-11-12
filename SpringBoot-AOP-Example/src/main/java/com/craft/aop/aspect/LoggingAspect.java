package com.craft.aop.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    // Runs before any method in BankService starts
    @Before("execution(* com.craft.aop.service.BankService.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("[LOG - Before] Calling method: " + joinPoint.getSignature().getName());
    }

    // Runs after any method in BankService finishes
    @After("execution(* com.craft.aop.service.BankService.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        System.out.println("[LOG - After] Finished method: " + joinPoint.getSignature().getName());
    }
}
